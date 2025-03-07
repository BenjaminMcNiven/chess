package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class MySqlGameDAO extends MySQLDAO implements GameDAO{

    public MySqlGameDAO(){
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL PRIMARY KEY,
              `whiteUsername` varchar(256) NULL,
              `blackUsername` varchar(256) NULL,
              `gameName` varchar(256) NULL,
              `game` LONGTEXT NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
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
            statement.setString(5, new Gson().toJson(newGame.game()));
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData getGame(int gameId) {
        String getGame = "SELECT gameID,whiteUsername,blackUsername,gameName,game from games WHERE gameID=? ;";
        GameData fetchedGame;
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(getGame)) {
            statement.setString(1, String.valueOf(gameId));
            List<Map<String, Object>> queryResult = executeQuerySQL(statement);
            if(queryResult.isEmpty()) {
                return null;
            } else if (queryResult.size()>1) {
                throw new RuntimeException("Multiple Games returned");
            }
            Map<String,Object> result = queryResult.getFirst();
            fetchedGame=new GameData((Integer) result.get("gameID"),(String)result.get("whiteUsername"),(String) result.get("blackUsername"),(String)result.get("gameName"),new Gson().fromJson((String) result.get("game"), ChessGame.class));
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return fetchedGame;
    }

    @Override
    public Collection<GameData> listGames() {
        String getGame = "SELECT gameID,whiteUsername,blackUsername,gameName,game from games;";
        List<GameData> games=new ArrayList<>();
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(getGame)) {
            List<Map<String, Object>> queryResult = executeQuerySQL(statement);
            for(Map<String, Object> result:queryResult){
                System.out.println(result);
                games.add(new GameData((Integer) result.get("gameID"),(String)result.get("whiteUsername"),(String) result.get("blackUsername"),(String)result.get("gameName"),new Gson().fromJson((String) result.get("game"), ChessGame.class)));
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return games;
    }

    @Override
    public void updateGame(GameData game) {
        String updateGame = "UPDATE games set whiteUsername=?,blackUsername=?,gameName=?,game=? WHERE gameID=?;";
        try (var conn = DatabaseManager.getConnection(); PreparedStatement statement = conn.prepareStatement(updateGame)) {
            statement.setString(5, String.valueOf(game.gameID()));
            statement.setString(1, game.whiteUsername());
            statement.setString(2, game.blackUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, new Gson().toJson(game.game()));
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
