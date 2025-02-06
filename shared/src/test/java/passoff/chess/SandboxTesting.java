package passoff.chess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class SandboxTesting {

    public static void main(String[] args) throws CloneNotSupportedException {
        ChessGame game=new ChessGame();
        ChessBoard board=game.getBoard();
        System.out.println(game);
        ChessPiece piece=board.getPiece(new ChessPosition(7,6));
        System.out.println(piece.clone());
    }
}
