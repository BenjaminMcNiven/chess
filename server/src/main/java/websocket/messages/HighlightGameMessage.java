package websocket.messages;

import chess.ChessGame;
import chess.ChessPosition;

public class HighlightGameMessage extends ServerMessage{

    private final ChessGame game;
    private final ChessPosition pos;

    public HighlightGameMessage(ServerMessageType type, ChessGame game, ChessPosition pos) {
        super(type);
        this.game=game;
        this.pos=pos;
    }
    public ChessGame getGame() {
        return game;
    }

    public ChessPosition getPos() {
        return pos;
    }
}
