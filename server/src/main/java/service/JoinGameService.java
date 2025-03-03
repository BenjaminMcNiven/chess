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
        GameData gameDat=gameDAO.getGame(request.gameID());
        switch (request.playerColor()){
            case "BLACK"-> {
                if (gameDat.blackUsername() != null) {
                    throw new DataAccessException("Error: already taken");
                }
                GameData newGame = new GameData(request.gameID(), gameDat.whiteUsername(), authData.username(), gameDat.gameName(), gameDat.game());
                gameDAO.updateGame(newGame);
            }
            case "WHITE"->{
                if(gameDat.whiteUsername()!=null) {
                    throw new DataAccessException("Error: already taken");
                }
                GameData newGame=new GameData(request.gameID(), authData.username(),gameDat.blackUsername(),gameDat.gameName(),gameDat.game());
                gameDAO.updateGame(newGame);
            }
        }
    }

}
