package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    @Test
    void registerNegative(){
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        Assertions.assertThrows(DataAccessException.class, ()-> registerService.register(new UserData("username", "password", null)));
    }

    @Test
    void logoutPositive() {

    }
    @Test
    void logoutNegative() {

    }

    @Test
    void loginPositive() {

    }
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