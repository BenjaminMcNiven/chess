package server.websocket;

import com.google.gson.Gson;
import commands.UserGameCommand;
import dataaccess.*;
import messages.LoadGameMessage;
import messages.NotificationMessage;
import messages.ServerMessage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler() {
        this.authDAO = new MySqlAuthDAO();
        this.userDAO = new MySqlUserDAO();
        this.gameDAO = new MySqlGameDAO();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
//            case MAKE_MOVE -> makeMove();
//            case LEAVE -> leave();
//            case RESIGN -> resign();
        }
    }

    private void connect(String authToken, int gameID, Session session) throws IOException {
        LoadGameMessage lgm=new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameDAO.getGame(gameID).game());
        String visitorName=authDAO.getAuth(authToken).username();
        connections.add(visitorName, session);
        var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,visitorName+" joined the game");
        connections.broadcast(visitorName, message);
    }

//     Petshop example for reference
//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}