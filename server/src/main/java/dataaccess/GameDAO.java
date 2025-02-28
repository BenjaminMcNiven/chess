package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();
    void createGame(GameData newGame);
    GameData getGame(int gameId);
    Collection<GameData> listGames();
    void updateGame(GameData game);
}
