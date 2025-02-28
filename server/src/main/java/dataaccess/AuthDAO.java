package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData newAuth);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
