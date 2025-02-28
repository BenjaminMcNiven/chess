package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    final private HashMap<Integer, UserData> users = new HashMap<>();

    public void createUser(UserData newUser){
        users.put(newUser.username().hashCode(),newUser);
    }
    public UserData getUser(String username){
        return users.get(username.hashCode());
    }
    public void clear(){
        users.clear();
    }
}
