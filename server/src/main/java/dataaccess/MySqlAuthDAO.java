package dataaccess;

import model.AuthData;

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
        String[] clear = new String[]{
                """
                DELETE FROM auths;
                """
        };
        executeSQL(clear);
    }

    @Override
    public void createAuth(AuthData newAuth) {
        String[] createAuth = new String[]{String.format(
                """
                INSERT INTO auths (authToken, username) VALUES (%s, %s);
                """,newAuth.authToken(),newAuth.username())
        };
        executeSQL(createAuth);
    }

    @Override
    public AuthData getAuth(String authToken) {
        String getAuth = String.format("SELECT authToken, username from auths WHERE authToken=%s ;",authToken);
        List<Map<String, Object>> queryResult = executeQuerySQL(getAuth);
        System.out.println(queryResult);
        return new AuthData("","");
    }

    @Override
    public void deleteAuth(String authToken) {
        String[] createAuth = new String[]{String.format(
                """
                DELETE FROM auths WHERE authToken=%s;
                """,authToken)
        };
        executeSQL(createAuth);
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
            executeSQL(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    private void executeSQL(String[] toExecuteSQL) {
        for (var statement : toExecuteSQL) {
            try (var conn = DatabaseManager.getConnection(); var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            } catch (DataAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Map<String, Object>> executeQuerySQL(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {
            var metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                results.add(row);
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}
