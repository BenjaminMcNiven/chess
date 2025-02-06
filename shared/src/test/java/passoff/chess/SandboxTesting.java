package passoff.chess;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public class SandboxTesting {

    public static void main(String[] args) throws CloneNotSupportedException {
        ChessBoard board=new ChessBoard();
        board.resetBoard();
        System.out.println(board);

        ChessPiece piece=board.getPiece(new ChessPosition(7,6));
        System.out.println(piece.clone());

    }
}
