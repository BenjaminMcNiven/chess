package websocket.commands;

import chess.ChessGame;

public class LeaveCommand extends UserGameCommand{

    private final ChessGame.TeamColor color;

    public LeaveCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor color) {
        super(commandType, authToken, gameID);
        this.color = color;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
