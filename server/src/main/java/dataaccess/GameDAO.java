package dataaccess;

import model.GameData;

public interface GameDAO {
    void clear();
    void createGame(GameData newGame);
    void getGame(GameData game);
    void listGames();
    void updateGame(GameData game);
}
