package websocket.commands;

import chess.ChessGame;

public class ResignCommand extends UserGameCommand{

    private final ChessGame.TeamColor color;

    public ResignCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor color) {
        super(commandType, authToken, gameID);
        this.color = color;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
