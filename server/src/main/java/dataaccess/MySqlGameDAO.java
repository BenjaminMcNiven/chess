package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO{

    public MySqlGameDAO() throws Exception {
        configureDatabase();
    }

    @Override
    public void clear() {

    }

    @Override
    public void createGame(GameData newGame) {

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
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL PRIMARY KEY,
              `whiteUsername` varchar(256) NULL,
              `blackUsername` varchar(256) NULL,
              `Username` varchar(256) NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
