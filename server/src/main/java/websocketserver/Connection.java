package websocketserver;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public int gameID;

    public Connection(String visitorName, Session session, int gameID) {
        this.visitorName = visitorName;
        this.session = session;
        this.gameID=gameID;
    }

    public void send(ServerMessage serverMessage) throws IOException {
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }
}