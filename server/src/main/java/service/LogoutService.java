package service;

import dataaccess.AuthDAO;

public class LogoutService {

    public AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(String authToken){
        authDAO.deleteAuth(authToken);
    }
}
