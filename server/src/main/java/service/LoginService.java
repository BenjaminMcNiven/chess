package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class LoginService {

    public UserDAO userDAO;
    public AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(UserData request) throws DataAccessException {
        UserData checkUser=userDAO.getUser(request.username());
        if(checkUser==null || !Objects.equals(checkUser.username(), request.username()) || !Objects.equals(checkUser.password(), request.password())){
            throw new DataAccessException("Error: unauthorized");
        }
        AuthData authData=new AuthData(UUID.randomUUID().toString(),request.username());
        authDAO.createAuth(authData);
        return authData;
    }
}
