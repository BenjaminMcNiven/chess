package service;

import dataaccess.AuthDAO;

public class AuthenticateService {

    public AuthDAO authDAO;

    public AuthenticateService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public boolean authenticated(String authToken){
        return authDAO.getAuth(authToken)!=null;
    }
}
