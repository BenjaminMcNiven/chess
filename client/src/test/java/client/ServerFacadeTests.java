package client;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import facade.ServerFacade;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"+port);
        facade.clearDatabase();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Order(1)
    @Test
    void register_Success() throws ResponseException {
        UserData newUser = new UserData("testUser", "password", "email");
        AuthData result = facade.register(newUser);

        assertNotNull(result);
        assertNotNull(result.authToken());
        assertEquals("testUser", result.username());
    }

    @Order(2)
    @Test
    void registerFailure() {
        UserData newUser = new UserData(null, null,null);
        assertThrows(ResponseException.class, () -> facade.register(newUser));
    }

    @Order(3)
    @Test
    void loginUserSuccess() throws ResponseException {
        UserData loginUser = new UserData("testUser", "password",null);
        AuthData result = facade.loginUser(loginUser);

        assertNotNull(result);
        assertNotNull(result.authToken());
        assertEquals("testUser", result.username());
    }

    @Order(4)
    @Test
    void loginUserFailure() {
        UserData loginUser = new UserData("wrongUser", "wrongPassword",null);
        assertThrows(ResponseException.class, () -> facade.loginUser(loginUser));
    }

    @Order(5)
    @Test
    void logoutUserSuccess() throws ResponseException {
        facade.logoutUser();
        assertThrows(ResponseException.class,()->facade.getActiveGame());
    }

    @Order(6)
    @Test
    void observeGameFailure() {
        assertThrows(ResponseException.class, () -> facade.observe(1));
        try{
            facade.observe(1);
        } catch (ResponseException e) {
            assert(e.getStatusCode()>300);
        }
    }

    @Order(7)
    @Test
    void listGamesSuccess() throws ResponseException {
        facade.register(new UserData("testingUser", "password","email"));
        HashMap<Integer, GameData> result = facade.listGames();
        assertNotNull(result);
    }

    @Order(8)
    @Test
    void listGamesFailure() {
        try {
            facade.logoutUser();
        } catch (ResponseException e) {
            fail();
        }
        assertThrows(ResponseException.class, () -> facade.listGames());
    }

    @Order(9)
    @Test
    void observeGameSuccess() throws ResponseException {
        facade.register(new UserData("testUsers", "password","email"));
        facade.listGames();
        assertDoesNotThrow(() -> facade.observe(1));
    }

    @Order(10)
    @Test
    void createGameSuccess() throws ResponseException {
        facade.register(new UserData("testUses", "password","email"));
        assertDoesNotThrow(() -> facade.createGame("TestGame"));
    }

    @Order(11)
    @Test
    void getActiveGameSuccess() throws ResponseException {
        facade.register(new UserData("testUse", "password","email"));
        facade.listGames();
        facade.observe(1);
        assertNotNull(facade.getActiveGame());
    }

    @Order(12)
    @Test
    void getActiveGameFailure() throws ResponseException {
        facade.logoutUser();
        assertThrows(ResponseException.class, ()->facade.getActiveGame());
    }

    @Order(13)
    @Test
    void createGameFailure(){
        assertThrows(ResponseException.class, () -> facade.createGame("TestGame"));
    }

    @Order(14)
    @Test
    void joinGameSuccess() throws ResponseException {
        facade.register(new UserData("user", "password","email"));
//        Create a bunch of new games to make sure the game we try to join is free
        for(int count=0; count<20; count++){
            facade.createGame("TestGame");
        }
        facade.listGames();
        assertDoesNotThrow(() -> facade.joinGame("WHITE", 20));
    }

    @Order(15)
    @Test
    void joinGameFailure() throws ResponseException {
        facade.logoutUser();
        assertThrows(ResponseException.class, () -> facade.joinGame("WHITE", 999));
    }

}
