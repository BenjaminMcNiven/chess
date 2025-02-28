package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<Integer, AuthData> auths = new HashMap<>();

    public void createAuth(AuthData newAuth){
        auths.put(newAuth.authToken().hashCode(),newAuth);
    }
    public AuthData getAuth(String authToken){
        return auths.get(authToken.hashCode());
    }
    public void clear(){
        auths.clear();
    }
    public void deleteAuth(String authToken){
        auths.remove(authToken.hashCode());
    }
}
