package service;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public class UserService {
    public UserData register(UserData registerRequest){return new UserData("","","");}
    public UserData login(UserData loginRequest){return new UserData("","","");}
    public void logout(UserData logoutRequest){}
}
