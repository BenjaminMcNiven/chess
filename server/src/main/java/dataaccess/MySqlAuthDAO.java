package dataaccess;

import model.AuthData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlAuthDAO implements AuthDAO{

    public MySqlAuthDAO() {
        configureDatabase();
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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auths (
              `authToken` varchar(256) NOT NULL PRIMARY KEY,
              `username` varchar(256) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws RuntimeException {
        try{
            DatabaseManager.createDatabase();
            for (var statement : createStatements) {
                var conn = DatabaseManager.getConnection();
                var preparedStatement = conn.prepareStatement(statement);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    public List<Map<String, Object>> executeQuerySQL(PreparedStatement statement) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (var resultSet = statement.executeQuery()) {
            var metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}
