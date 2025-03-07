package dataaccess;

import model.UserData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MySqlUserDAO extends MySQLDAO implements UserDAO {

    public MySqlUserDAO() {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL PRIMARY KEY,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    @Override
    public void clear() {
        String clear = "DELETE FROM users;";
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(clear)) {
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData newUser) {
        String createUser = "INSERT INTO users (username,password,email) VALUES (?, ?, ?);";
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(createUser)) {
            statement.setString(1, newUser.username());
            statement.setString(2, newUser.password());
            statement.setString(3, newUser.email());
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData getUser(String username) {
        String getUser = "SELECT username,password,email from users WHERE username=? ;";
        UserData fetchedUser;
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(getUser)) {
            statement.setString(1, username);
            List<Map<String, Object>> queryResult = executeQuerySQL(statement);
            if (queryResult.isEmpty()) {
                return null;
            } else if (queryResult.size() > 1) {
                throw new RuntimeException("Multiple Users returned");
            }
            Map<String, Object> result = queryResult.getFirst();
            fetchedUser = new UserData((String) result.get("username"), (String) result.get("password"), (String) result.get("email"));
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return fetchedUser;
    }

}