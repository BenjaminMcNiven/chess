package websocket.commands;

import chess.ChessPosition;

public class HighlightCommand extends UserGameCommand{

    private final ChessPosition pos;

    public HighlightCommand(CommandType commandType, String authToken, Integer gameID, ChessPosition pos) {
        super(commandType, authToken, gameID);
        this.pos = pos;
    }

    public ChessPosition getPos() {
        return pos;
    }
}
