package service;

import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class ListGamesService {
    public GameDAO gameDAO;

    public ListGamesService(GameDAO gameDAO){
        this.gameDAO=gameDAO;
    }

    public Collection<GameData> listGames(){
        return gameDAO.listGames();
    }
}
