package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {

    public UserDAO userDAO;
    public AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData request) throws DataAccessException {
        UserData user = userDAO.getUser(request.username());
        if(user!=null){
            throw new DataAccessException("Error: already taken");
        }
        userDAO.createUser(request);
        AuthData authData=new AuthData(UUID.randomUUID().toString(),request.username());
        authDAO.createAuth(authData);
        return authData;
    }
}
