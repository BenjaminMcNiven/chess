package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        System.out.println(user);
        if(user!=null){
            throw new DataAccessException("Error: already taken");
        }
        var hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        UserData newUser=new UserData(request.username(), hashedPassword, request.email());
        userDAO.createUser(newUser);
        AuthData authData=new AuthData(UUID.randomUUID().toString(),request.username());
        authDAO.createAuth(authData);
        return authData;
    }
}
