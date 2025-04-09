package websocket;

import chess.*;
import com.google.gson.Gson;
import websocket.commands.HighlightCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import dataaccess.*;
import websocket.messages.HighlightGameMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
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

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case MAKE_MOVE -> {
                ChessMove move=new Gson().fromJson(message, MakeMoveCommand.class).getMove();
                makeMove(command.getAuthToken(), command.getGameID(),move,session);
            }
            case LEAVE -> {
                ChessGame.TeamColor color=new Gson().fromJson(message, LeaveCommand.class).getColor();
                leave(command.getAuthToken(), command.getGameID(), color);
            }
            case RESIGN -> {
                ChessGame.TeamColor color=new Gson().fromJson(message, LeaveCommand.class).getColor();
                resign(command.getAuthToken(), command.getGameID(), color, session);
            }
            case REDRAW -> redraw(command.getAuthToken(), command.getGameID(), session);
            case HIGHLIGHT -> {
                ChessPosition pos=new Gson().fromJson(message, HighlightCommand.class).getPos();
                highlight(command.getAuthToken(), command.getGameID(), session, pos);
            }

        }
    }

    private void connect(String authToken, int gameID, Session session) throws IOException {
        LoadGameMessage lgm=new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameDAO.getGame(gameID).game());
        session.getRemote().sendString(new Gson().toJson(lgm));
        String visitorName=authDAO.getAuth(authToken).username();
        connections.add(visitorName, session);
        var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,visitorName+" joined the game");
        connections.broadcast(visitorName, message);
    }

    private void leave(String authToken, int gameID, ChessGame.TeamColor color) throws IOException {
        String visitorName=authDAO.getAuth(authToken).username();
        GameData game=gameDAO.getGame(gameID);
        if(color== ChessGame.TeamColor.WHITE){
            if(visitorName.equals(game.whiteUsername())){
                gameDAO.updateGame(new GameData(game.gameID(),null,game.blackUsername(),game.gameName(),game.game()));
            }
        }else{
            if(visitorName.equals(game.blackUsername())){
                gameDAO.updateGame(new GameData(game.gameID(),game.whiteUsername(),null,game.gameName(),game.game()));
            }
        }
        connections.remove(visitorName);
        var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,visitorName+" left the game");
        connections.broadcast(visitorName, message);
    }

    private void makeMove(String authToken, int gameID, ChessMove move, Session session) throws IOException, InvalidMoveException {
        GameData gameData= gameDAO.getGame(gameID);
        ChessGame game=gameData.game();
        game.makeMove(move);
        GameData newGame=new GameData(gameData.gameID(),gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),game);
        gameDAO.updateGame(newGame);
        LoadGameMessage lgm=new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,newGame.game());
        session.getRemote().sendString(new Gson().toJson(lgm));
        String visitorName=authDAO.getAuth(authToken).username();
        var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,visitorName+" made the move "+move);
        connections.broadcast(visitorName, message);
    }

    private void resign(String authToken, int gameID, ChessGame.TeamColor color, Session session) throws IOException, InvalidMoveException {
        GameData gameData= gameDAO.getGame(gameID);
        ChessGame game=gameData.game();
        if(game.getState()==ChessState.PLAY){
            if(color==ChessGame.TeamColor.WHITE){
                game.setState(ChessState.WR);
            }else{
                game.setState(ChessState.BR);
            }
        }
        session.getRemote().sendString(String.valueOf(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,"You resigned the game.")));
        String visitorName=authDAO.getAuth(authToken).username();
        var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,visitorName+" joined the game");
        connections.broadcast(visitorName, message);
    }

    private void redraw(String authToken, int gameID, Session session) throws IOException {
        LoadGameMessage lgm=new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameDAO.getGame(gameID).game());
        session.getRemote().sendString(new Gson().toJson(lgm));
    }

    private void highlight(String authToken, int gameID, Session session,ChessPosition pos) throws IOException {
        HighlightGameMessage hgm=new HighlightGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameDAO.getGame(gameID).game(),pos);
        session.getRemote().sendString(new Gson().toJson(hgm));
    }

}