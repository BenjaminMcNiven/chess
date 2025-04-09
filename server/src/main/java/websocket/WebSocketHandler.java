package websocket;

import chess.*;
import com.google.gson.Gson;
import model.AuthData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import websocket.commands.HighlightCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import dataaccess.*;
import websocket.messages.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, error.getMessage());
        session.getRemote().sendString(new Gson().toJson(errorMessage));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case MAKE_MOVE -> {
                ChessMove move=new Gson().fromJson(message, MakeMoveCommand.class).getMove();
                makeMove(command.getAuthToken(), command.getGameID(),move,session);
            }
            case LEAVE -> leave(command.getAuthToken(), command.getGameID());
            case RESIGN -> resign(command.getAuthToken(), command.getGameID(), session);
            case REDRAW -> redraw(command.getAuthToken(), command.getGameID(), session);
            case HIGHLIGHT -> {
                ChessPosition pos=new Gson().fromJson(message, HighlightCommand.class).getPos();
                highlight(command.getAuthToken(), command.getGameID(), session, pos);
            }

        }
    }

    private void connect(String authToken, int gameID, Session session) throws IOException {
        if(unauthorized(authToken)){
            throw new RuntimeException("Unauthorized to join game");
        }
        GameData gameData = gameDAO.getGame(gameID);
        if(gameData==null){
            throw new RuntimeException("Game does not Exist");
        }
        String visitorName=authDAO.getAuth(authToken).username();
        connections.add(visitorName, session,gameID);
        LoadGameMessage lgm=new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameData.game());
        session.getRemote().sendString(new Gson().toJson(lgm));
        var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,visitorName+" joined the game");
        connections.broadcast(visitorName, message);
    }

    private void leave(String authToken, int gameID) throws IOException {
        String visitorName=authDAO.getAuth(authToken).username();
        GameData game=gameDAO.getGame(gameID);
        ChessGame.TeamColor color=getColor(authToken, gameID);
        if(color==ChessGame.TeamColor.WHITE){
            if(visitorName.equals(game.whiteUsername())){
                gameDAO.updateGame(new GameData(game.gameID(),null,game.blackUsername(),game.gameName(),game.game()));
            }
        }else{
            if(visitorName.equals(game.blackUsername())){
                gameDAO.updateGame(new GameData(game.gameID(),game.whiteUsername(),null,game.gameName(),game.game()));
            }
        }
        var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,visitorName+" left the game");
        connections.broadcast(visitorName, message);
        connections.remove(visitorName);
    }

    private void makeMove(String authToken, int gameID, ChessMove move, Session session) throws IOException, InvalidMoveException {
        if(unauthorized(authToken)){
            throw new RuntimeException("Unauthorized to join game");
        }
        GameData gameData=gameDAO.getGame(gameID);
        if(gameData==null){
            throw new RuntimeException("Game does not Exist");
        }
        ChessGame game=gameData.game();
        if(game.getState()!=ChessState.PLAY){
            throw new RuntimeException("Game is already over");
        }
        String visitorName=authDAO.getAuth(authToken).username();
        if(visitorName.equals(gameData.blackUsername())&&game.getTeamTurn()!=ChessGame.TeamColor.BLACK){
            throw new RuntimeException("Not your turn");
        }
        if(visitorName.equals(gameData.whiteUsername())&&game.getTeamTurn()!=ChessGame.TeamColor.WHITE){
            throw new RuntimeException("Not your turn");
        }
        if(!visitorName.equals(gameData.whiteUsername()) && !visitorName.equals(gameData.blackUsername())){
            throw new RuntimeException("You are an observer");
        }
        game.makeMove(move);
        GameData newGame=new GameData(gameData.gameID(),gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),game);
        gameDAO.updateGame(newGame);
        LoadGameMessage lgm=new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,newGame.game());
        session.getRemote().sendString(new Gson().toJson(lgm));
        connections.broadcast(visitorName,lgm);
        var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,visitorName+" made the move "+move);
        connections.broadcast(visitorName, message);
    }

    private void resign(String authToken, int gameID, Session session) throws IOException{
        if(unauthorized(authToken)){
            throw new RuntimeException("Unauthorized to join game");
        }
        String visitorName=authDAO.getAuth(authToken).username();
        GameData gameData=gameDAO.getGame(gameID);
        ChessGame game=gameData.game();
        if(!visitorName.equals(gameData.whiteUsername()) && !visitorName.equals(gameData.blackUsername())){
            throw new RuntimeException("You are an observer");
        }
        if(game.getState()==ChessState.PLAY){
            ChessGame.TeamColor color=getColor(authToken,gameID);
            if(color==ChessGame.TeamColor.WHITE){
                game.setState(ChessState.WR);
            }else{
                game.setState(ChessState.BR);
            }
            gameDAO.updateGame(new GameData(gameData.gameID(),gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),game));
        }
        else{
            throw new RuntimeException("Game is already over");
        }
        NotificationMessage notificationMessage=new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,"You resigned the game.");
        session.getRemote().sendString(new Gson().toJson(notificationMessage));
        var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,visitorName+" joined the game");
        connections.broadcast(visitorName, message);
    }

    private void redraw(String authToken, int gameID, Session session) throws IOException {
        if(unauthorized(authToken)){
            throw new RuntimeException("Unauthorized to join game");
        }
        LoadGameMessage lgm=new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameDAO.getGame(gameID).game());
        session.getRemote().sendString(new Gson().toJson(lgm));
    }

    private void highlight(String authToken, int gameID, Session session,ChessPosition pos) throws IOException {
        if(unauthorized(authToken)){
            throw new RuntimeException("Unauthorized to join game");
        }
        HighlightGameMessage hgm=new HighlightGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameDAO.getGame(gameID).game(),pos);
        session.getRemote().sendString(new Gson().toJson(hgm));
    }

    private boolean unauthorized(String authToken){
        return authDAO.getAuth(authToken) == null;
    }

    public ChessGame.TeamColor getColor(String authToken, int gameID){
        AuthData authData=authDAO.getAuth(authToken);
        GameData gameData=gameDAO.getGame(gameID);
        if(gameData==null){
            throw new RuntimeException("Game does not Exist");
        }
        if(authData.username().equals(gameData.whiteUsername())){
            return ChessGame.TeamColor.WHITE;
        }
        if(authData.username().equals(gameData.blackUsername())){
            return ChessGame.TeamColor.BLACK;
        }
        else{
            return null;
        }
    }

}