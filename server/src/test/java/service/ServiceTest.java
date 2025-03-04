package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

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
            Assertions.assertEquals(expectedUserData,observedUserData);
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

    }
    @Order(3)
    @Test
    void logoutNegative() {

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

    }
    @Test
    void listGamesPositive() {

    }
    @Test
    void listGamesNegative() {

    }
    @Test
    void joinGamePositive() {

    }
    @Test
    void joinGameNegative() {

    }
    @Test
    void createGamePositive() {

    }
    @Test
    void createGameNegative() {

    }
    @Test
    void clearDatabasePositive() {

    }
    @Test
    void clearDatabaseNegative() {

    }
    @Test
    void authenticatePositive() {

    }
    @Test
    void authenticateNegative() {

    }
}