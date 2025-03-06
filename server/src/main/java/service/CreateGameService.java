package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import java.util.Random;

public class CreateGameService extends AuthenticatedService{

    public GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO,GameDAO gameDAO) {
        super(authDAO);
        this.gameDAO = gameDAO;
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        if(unauthorized(authToken)){
            throw new DataAccessException("Error: unauthorized");
        }
        Random random=new Random();
        int gameID=random.nextInt(Integer.MAX_VALUE - 1) + 1;
        while(gameDAO.getGame(gameID)!=null){
            gameID = random.nextInt(Integer.MAX_VALUE - 1) + 1;
        }
        GameData gameData=new GameData(gameID,null,null,gameName, new ChessGame());
        gameDAO.createGame(gameData);
        return gameData.gameID();
    }
}
