package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import java.util.Random;

public class CreateGameService {

    public GameDAO gameDAO;
    public AuthDAO authDAO;

    public CreateGameService(AuthDAO authDAO,GameDAO gameDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public int createGame(String gameName){
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
