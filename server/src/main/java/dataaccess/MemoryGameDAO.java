package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    final private HashMap<Integer, GameData> games = new HashMap<>();

    public void createGame(GameData newGame){
        System.out.println(newGame);
        games.put(newGame.gameID(),newGame);
    }
    public GameData getGame(int gameId){
        return games.get(gameId);
    }
    public Collection<GameData> listGames(){
        System.out.println("List Games DAO: \n"+games.values());
        return games.values();
    }
    public void updateGame(GameData game){
        games.put(game.gameID(), game);
    }
    public void clear(){
        games.clear();
    }
}
