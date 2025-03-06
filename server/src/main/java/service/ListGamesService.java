package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class ListGamesService extends AuthenticatedService {
    public GameDAO gameDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO){
        super(authDAO);
        this.gameDAO=gameDAO;
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if(unauthorized(authToken)){
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }
}
