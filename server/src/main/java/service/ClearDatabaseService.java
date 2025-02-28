package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearDatabaseService {

    public UserDAO userDAO;
    public AuthDAO authDAO;
    public GameDAO gameDAO;

    public ClearDatabaseService(UserDAO userDAO, AuthDAO authDAO,GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void clear(){
            userDAO.clear();
            gameDAO.clear();
            authDAO.clear();
    }
}
