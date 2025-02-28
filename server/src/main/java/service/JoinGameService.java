package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import server.JoinGameRequest;

public class JoinGameService {
    public AuthDAO authDAO;
    public GameDAO gameDAO;

    public JoinGameService(AuthDAO authdao, GameDAO gameDAO){
        this.authDAO=authdao;
        this.gameDAO=gameDAO;
    }

    public void joinGame(String authToken, JoinGameRequest request) throws DataAccessException {
        AuthData authData=authDAO.getAuth(authToken);
        GameData gameData=gameDAO.getGame(request.gameID());
        switch (request.playerColor()){
            case "BLACK"-> {
                if (gameData.blackUsername() != null) {
                    throw new DataAccessException("Error: already taken");
                }
                GameData newGame = new GameData(request.gameID(), gameData.whiteUsername(), authData.username(), gameData.gameName(), gameData.game());
                gameDAO.updateGame(newGame);
            }
            case "WHITE"->{
                if(gameData.whiteUsername()!=null) {
                    throw new DataAccessException("Error: already taken");
                }
                GameData newGame=new GameData(request.gameID(), authData.username(),gameData.blackUsername(),gameData.gameName(),gameData.game());
                gameDAO.updateGame(newGame);
            }
        }
    }

}
