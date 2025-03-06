package dataaccess;

import model.GameData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class MySqlGameDAO implements GameDAO{

    public MySqlGameDAO(){
        configureDatabase();
    }

    @Override
    public void clear() {
        String clear = "DELETE FROM games;";
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(clear)) {
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(GameData newGame) {
        String createUser = "INSERT INTO games (gameID,whiteUsername,blackUsername,gameName,game) VALUES (?, ?, ?,?,?);";
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(createUser)) {
            statement.setString(1, String.valueOf(newGame.gameID()));
            statement.setString(2, newGame.whiteUsername());
            statement.setString(3, newGame.blackUsername());
            statement.setString(4, newGame.gameName());
            statement.setString(5, String.valueOf(newGame.game()));
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData getGame(int gameId) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL PRIMARY KEY,
              `whiteUsername` varchar(256) NULL,
              `blackUsername` varchar(256) NULL,
              `gameName` varchar(256) NULL,
              `game` TEXT NOT NULL
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
