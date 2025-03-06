package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.mindrot.jbcrypt.BCrypt;
import server.JoinGameRequest;

import java.util.Collection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class ServiceTest {

    public static UserDAO userDAO;
    public static AuthDAO authDAO;
    public static GameDAO gameDAO;

    @BeforeAll
    static void setDAOs(){
        userDAO=new MemoryUserDAO();
        authDAO=new MemoryAuthDAO();
        gameDAO=new MemoryGameDAO();
    }
    @Order(1)
    @Test
    void registerPositive(){
        try {
            RegisterService registerService = new RegisterService(userDAO, authDAO);
            AuthData newAuth=registerService.register(new UserData("username", "password", "email@email.com"));
            UserData expectedUserData=new UserData("username","password","email@email.com");
            UserData observedUserData=userDAO.getUser("username");
            Assertions.assertEquals(expectedUserData.email(),observedUserData.email());
            Assertions.assertEquals(expectedUserData.username(),observedUserData.username());
            Assertions.assertTrue(BCrypt.checkpw(expectedUserData.password(),observedUserData.password()));
            Assertions.assertNotNull(newAuth);
        }catch (Exception e){
            Assertions.fail();
        }
    }
    @Order(2)
    @Test
    void registerNegative(){
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        Assertions.assertThrows(DataAccessException.class, ()-> registerService.register(new UserData("username", "password", null)));
    }
    @Order(4)
    @Test
    void logoutPositive() {
        try {
            LoginService loginService = new LoginService(userDAO,authDAO);
            LogoutService logoutService = new LogoutService(authDAO);
            AuthData authToken = loginService.login(new UserData("username","password",null));
            logoutService.logout(authToken.authToken());
            Assertions.assertNull(authDAO.getAuth(authToken.authToken()));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }
    @Order(3)
    @Test
    void logoutNegative() {
//        is there a way to fail the logout, no right??!??, not from the service class
    }
    @Order(5)
    @Test
    void loginPositive() {
        try {
            LoginService loginService = new LoginService(userDAO,authDAO);
            AuthData authToken = loginService.login(new UserData("username","password",null));
            AuthData expected = new AuthData(authToken.authToken(),"username");
            AuthData observed = authDAO.getAuth(authToken.authToken());
            Assertions.assertEquals(expected,observed);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }
    @Order(6)
    @Test
    void loginNegative() {
        LoginService loginService = new LoginService(userDAO, authDAO);
        Assertions.assertThrows(DataAccessException.class, ()-> loginService.login(new UserData("username", "passwor", null)));
    }
    @Order(7)
    @Test
    void createGamePositive() {
        try {
            LoginService loginService = new LoginService(userDAO,authDAO);
            AuthData authToken = loginService.login(new UserData("username","password",null));
            CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
            int gameID = createGameService.createGame(authToken.authToken(), "game");
            Collection<GameData> games = gameDAO.listGames();
            Assertions.assertTrue(games.contains(new GameData(gameID, null, null, "game", new ChessGame())));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }
    @Order(8)
    @Test
    void listGamesPositive() {
        try {
            ListGamesService listGamesService=new ListGamesService(authDAO,gameDAO);
            LoginService loginService = new LoginService(userDAO,authDAO);
            AuthData authToken = loginService.login(new UserData("username","password",null));
            Collection<GameData> games=listGamesService.listGames(authToken.authToken());
            Assertions.assertEquals(1, games.size());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }
    @Order(9)
    @Test
    void joinGamePositive() {
        try{
            JoinGameService joinGameService = new JoinGameService(authDAO,gameDAO);
            LoginService loginService = new LoginService(userDAO,authDAO);
            AuthData authToken = loginService.login(new UserData("username","password",null));
            CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
            int gameID = createGameService.createGame(authToken.authToken(), "game");
            joinGameService.joinGame(authToken.authToken(),new JoinGameRequest("WHITE",gameID));
            Collection<GameData> games = gameDAO.listGames();
            Assertions.assertTrue(games.contains(new GameData(gameID, "username", null, "game", new ChessGame())));
        }catch (DataAccessException e){
            Assertions.fail();
        }
    }
    @Order(10)
    @Test
    void joinGameNegative() {
        try {
            JoinGameService joinGameService = new JoinGameService(authDAO,gameDAO);
            LoginService loginService = new LoginService(userDAO,authDAO);
            AuthData auth = loginService.login(new UserData("username","password",null));
            CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
            int gameID = createGameService.createGame(auth.authToken(), "game");
            joinGameService.joinGame(auth.authToken(),new JoinGameRequest("WHITE",gameID));
            Assertions.assertThrows(DataAccessException.class,()->joinGameService.joinGame(auth.authToken(),new JoinGameRequest("WHITE",gameID)));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }
    @Order(13)
    @Test
    void clearDatabasePositive() {
        ClearDatabaseService clearDatabaseService=new ClearDatabaseService(userDAO,authDAO,gameDAO);
        clearDatabaseService.clear();
        Assertions.assertEquals(0, gameDAO.listGames().size());
    }
}