package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLTests {

    private MySqlAuthDAO authDAO;
    private MySqlUserDAO userDAO;
    private MySqlGameDAO gameDAO;

    @BeforeAll
    public void setUpDatabase() {
        authDAO = new MySqlAuthDAO();
        authDAO.clear();
        userDAO = new MySqlUserDAO();
        userDAO.clear();
        gameDAO = new MySqlGameDAO();
        gameDAO.clear();
    }

    @BeforeEach
    public void setUp() {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }

    @Test
    @Order(1)
    public void testCreateAuth_Success() {
        AuthData auth = new AuthData("token123", "user1");
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(auth));

        AuthData retrieved = authDAO.getAuth("token123");
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("token123", retrieved.authToken());
        Assertions.assertEquals("user1", retrieved.username());
    }

    @Test
    @Order(2)
    public void testCreateAuth_Failure_DuplicateToken() {
        AuthData auth = new AuthData("token123", "user1");
        authDAO.createAuth(auth);

        AuthData duplicateAuth = new AuthData("token123", "user2");
        Assertions.assertThrows(RuntimeException.class, () -> authDAO.createAuth(duplicateAuth));
    }

    @Test
    @Order(3)
    public void testGetAuth_Success() {
        AuthData auth = new AuthData("token456", "user2");
        authDAO.createAuth(auth);

        AuthData retrieved = authDAO.getAuth("token456");
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("token456", retrieved.authToken());
        Assertions.assertEquals("user2", retrieved.username());
    }

    @Test
    @Order(4)
    public void testGetAuth_Failure_NonExistentToken() {
        AuthData retrieved = authDAO.getAuth("nonexistent");
        Assertions.assertNull(retrieved);
    }

    @Test
    @Order(5)
    public void testDeleteAuth_Success() {
        AuthData auth = new AuthData("token789", "user3");
        authDAO.createAuth(auth);

        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth("token789"));
        Assertions.assertNull(authDAO.getAuth("token789"));
    }

    @Test
    @Order(6)
    public void testDeleteAuth_Failure_NonExistentToken() {
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth("nonexistent"));
    }

    @Test
    @Order(7)
    public void testClear_Success() {
        authDAO.createAuth(new AuthData("token1", "userA"));
        authDAO.createAuth(new AuthData("token2", "userB"));

        authDAO.clear();

        Assertions.assertNull(authDAO.getAuth("token1"));
        Assertions.assertNull(authDAO.getAuth("token2"));
    }

    @Test
    @Order(8)
    public void testCreateUser_Success() {
        UserData user = new UserData("user1", "password123", "user1@example.com");
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(user));

        UserData retrieved = userDAO.getUser("user1");
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("user1", retrieved.username());
        Assertions.assertEquals("password123", retrieved.password());
        Assertions.assertEquals("user1@example.com", retrieved.email());
    }

    @Test
    @Order(9)
    public void testCreateUser_Failure_DuplicateUsername() {
        UserData user = new UserData("user1", "password123", "user1@example.com");
        userDAO.createUser(user);

        UserData duplicateUser = new UserData("user1", "differentPass", "new@example.com");
        Assertions.assertThrows(RuntimeException.class, () -> userDAO.createUser(duplicateUser));
    }

    @Test
    @Order(10)
    public void testGetUser_Success() {
        UserData user = new UserData("user2", "securePass", "user2@example.com");
        userDAO.createUser(user);

        UserData retrieved = userDAO.getUser("user2");
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("user2", retrieved.username());
        Assertions.assertEquals("securePass", retrieved.password());
        Assertions.assertEquals("user2@example.com", retrieved.email());
    }

    @Test
    @Order(11)
    public void testGetUser_Failure_NonExistentUsername() {
        UserData retrieved = userDAO.getUser("nonexistent");
        Assertions.assertNull(retrieved);
    }

    @Test
    @Order(12)
    public void testUserClear_Success() {
        userDAO.createUser(new UserData("userA", "passA", "userA@example.com"));
        userDAO.createUser(new UserData("userB", "passB", "userB@example.com"));
        userDAO.clear();

        Assertions.assertNull(userDAO.getUser("userA"));
        Assertions.assertNull(userDAO.getUser("userB"));
    }

    @Test
    @Order(13)
    void testCreateGame_Success() {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Friendly Match", chessGame);

        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(game));

        GameData fetchedGame = gameDAO.getGame(1);
        Assertions.assertNotNull(fetchedGame);
        Assertions.assertEquals(1, fetchedGame.gameID());
        Assertions.assertEquals("whitePlayer", fetchedGame.whiteUsername());
        Assertions.assertEquals("blackPlayer", fetchedGame.blackUsername());
        Assertions.assertEquals("Friendly Match", fetchedGame.gameName());
    }

    @Test
    @Order(14)
    void testCreateGame_DuplicateID() {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(2, "playerA", "playerB", "Game 2", chessGame);
        gameDAO.createGame(game);

        Assertions.assertThrows(RuntimeException.class, () -> gameDAO.createGame(game));
    }

    @Test
    @Order(15)
    void testGetGame_NonExistent() {
        Assertions.assertNull(gameDAO.getGame(999));
    }

    @Test
    @Order(16)
    void testListGames() {
        ChessGame chessGame = new ChessGame();
        gameDAO.createGame(new GameData(3, "p1", "p2", "Third Game", chessGame));

        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertNotNull(games);
        Assertions.assertFalse(games.isEmpty());
    }

    @Test
    @Order(17)
    void testUpdateGame_Success() {
        ChessGame newGameState = new ChessGame();
        GameData game = new GameData(1, null, null, "Updated Game", newGameState);
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(game));
        try {
            newGameState.makeMove(new ChessMove(new ChessPosition(2,1),new ChessPosition(3,1)));
            GameData expected = new GameData(1, "newWhite", "newBlack", "Updated Game", newGameState);
            Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(expected));
        } catch (InvalidMoveException e) {
            Assertions.fail();
        }
        GameData observed = gameDAO.getGame(1);
        Assertions.assertNotNull(observed);
        Assertions.assertEquals("newWhite", observed.whiteUsername());
        Assertions.assertEquals("newBlack", observed.blackUsername());
        Assertions.assertEquals("Updated Game", observed.gameName());
    }


    @Test
    @Order(18)
    void testClearUsers() {
        userDAO.clear();
        Assertions.assertNull(userDAO.getUser("testUser"));
    }

    @Test
    @Order(19)
    void testClearGames() {
        gameDAO.clear();
        Assertions.assertNull(gameDAO.getGame(1));
    }
}
