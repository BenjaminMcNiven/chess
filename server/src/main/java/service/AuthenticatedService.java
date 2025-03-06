package service;

import dataaccess.AuthDAO;

public abstract class AuthenticatedService {

    public AuthDAO authDAO;

    public AuthenticatedService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public boolean unauthorized(String authToken){
        return authDAO.getAuth(authToken) == null;
    }
}
