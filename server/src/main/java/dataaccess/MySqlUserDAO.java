package dataaccess;

import model.UserData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlUserDAO implements UserDAO{

    public MySqlUserDAO() {
        configureDatabase();
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
            if(queryResult.isEmpty()) {
                return null;
            } else if (queryResult.size()>1) {
                throw new RuntimeException("Multiple Users returned");
            }
            Map<String,Object> result = queryResult.get(0);
            fetchedUser=new UserData((String) result.get("username"), (String) result.get("password"), (String) result.get("email"));
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return fetchedUser;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL PRIMARY KEY,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL
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
