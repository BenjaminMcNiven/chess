package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;

public class LogoutService extends AuthenticatedService{


    public LogoutService(AuthDAO authDAO) {
        super(authDAO);
    }

    public void logout(String authToken) throws DataAccessException {
        if(unauthorized(authToken)){
            throw new DataAccessException("Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}
