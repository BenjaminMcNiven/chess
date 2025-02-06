package passoff.chess;

import chess.*;

public class SandboxTesting {

    public static void main(String[] args){
        try {
            ChessGame game=new ChessGame();
            ChessBoard board=game.getBoard();
            System.out.println(game);
            ChessPiece piece=board.getPiece(new ChessPosition(7,6));
            System.out.println(piece.clone());
            game.makeMove(new ChessMove(new ChessPosition(1,4),new ChessPosition(7,6)));
            game.makeMove(new ChessMove(new ChessPosition(1,3),new ChessPosition(3,2)));
            ChessGame cloned= (ChessGame) game.clone();
            cloned.makeMove(new ChessMove(new ChessPosition(2,5),new ChessPosition(4,5)));
            System.out.println(game);
            System.out.println(cloned);
            System.out.println(game.isInCheckmate(ChessGame.TeamColor.BLACK));
            cloned.makeMove(new ChessMove(new ChessPosition(8,3),new ChessPosition(8,4)));
        } catch (CloneNotSupportedException | InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

}
