package dataaccess;

import model.AuthData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MySqlAuthDAO extends MySQLDAO implements AuthDAO{

    public MySqlAuthDAO() {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  auths (
              `authToken` varchar(256) NOT NULL PRIMARY KEY,
              `username` varchar(256) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }
    
    @Override
    public void clear() {
        String clear = "DELETE FROM auths;";
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(clear)) {
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createAuth(AuthData newAuth) {
        String createAuth = "INSERT INTO auths (authToken, username) VALUES (?, ?);";
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(createAuth)) {
            statement.setString(1, newAuth.authToken());
            statement.setString(2, newAuth.username());
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) {
        String getUser = "SELECT authToken,username from auths WHERE authToken=? ;";
        AuthData fetchedAuth;
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(getUser)) {
            statement.setString(1, authToken);
            List<Map<String, Object>> queryResult = executeQuerySQL(statement);
            if(queryResult.isEmpty()) {
                return null;
            } else if (queryResult.size()>1) {
                throw new RuntimeException("Multiple Auths returned");
            }
            Map<String,Object> result = queryResult.getFirst();
            fetchedAuth=new AuthData((String) result.get("authToken"), (String) result.get("username"));
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return fetchedAuth;
    }

    @Override
    public void deleteAuth(String authToken) {
        String deleteAuth ="DELETE FROM auths WHERE authToken=?;";
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(deleteAuth)) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
