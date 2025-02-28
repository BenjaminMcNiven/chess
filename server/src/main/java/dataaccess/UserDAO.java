package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear();
    void createUser(UserData newUser);
    UserData getUser(String username);
}
