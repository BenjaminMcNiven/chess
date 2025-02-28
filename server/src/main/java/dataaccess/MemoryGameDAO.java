package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    final private HashMap<Integer, GameData> games = new HashMap<>();

    public void createGame(GameData newGame){
        games.put(newGame.gameId(),newGame);
    }
    public GameData getGame(int gameId){
        return games.get(gameId);
    }
    public Collection<GameData> listGames(){
        return games.values();
    }
    public void updateGame(GameData game){
        games.put(game.gameId(), game);
    }
    public void clear(){
        games.clear();
    }
}
