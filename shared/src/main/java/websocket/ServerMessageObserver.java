package websocket;

import messages.ServerMessage;

public interface ServerMessageObserver {
    void notify(ServerMessage message);
}
