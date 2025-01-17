package passoff.chess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class TESTING {
    public static void main(String[] args){
        ChessBoard board=new ChessBoard();
        board.clearBoard();
        board.addPiece(new ChessPosition(8,3),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
        board.addPiece(new ChessPosition(7,3),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK));

        System.out.println(board);

        System.out.println(board.getPiece(new ChessPosition(7,3)).pieceMoves(board,new ChessPosition(7,3)));
    }
}
//        BLACK
//        rook | knight | bishop | queen | king | bishop | knight | rook |
//        pawn | pawn | pawn | pawn | pawn | pawn | pawn | pawn |
//        null | null | null | null | null | null | null | null |
//        null | null | null | null | null | null | null | null |
//        null | null | null | null | null | null | null | null |
//        null | null | null | null | null | null | null | null |
//        PAWN | PAWN | PAWN | PAWN | PAWN | PAWN | PAWN | PAWN |
//        ROOK | KNIGHT | BISHOP | QUEEN | KING | BISHOP | KNIGHT | ROOK |
//        WHITE