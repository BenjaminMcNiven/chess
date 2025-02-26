package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData newAuthData);
    void getAuth(AuthData authToken);
    void deleteAuth(AuthData authData);
}
