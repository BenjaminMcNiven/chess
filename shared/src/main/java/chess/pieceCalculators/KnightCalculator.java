package chess.pieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightCalculator implements MovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves=new ArrayList<>();

        ChessPosition upLeft = new ChessPosition(position.getRow()+2, position.getColumn()-1);
        if(checkMove(board,position,upLeft)){moves.add(new ChessMove(position,upLeft));}
        ChessPosition upRight = new ChessPosition(position.getRow()+2, position.getColumn()+1);
        if(checkMove(board,position,upRight)){moves.add(new ChessMove(position,upRight));}
        ChessPosition downLeft = new ChessPosition(position.getRow()-2, position.getColumn()-1);
        if(checkMove(board,position,downLeft)){moves.add(new ChessMove(position,downLeft));}
        ChessPosition downRight = new ChessPosition(position.getRow()-2, position.getColumn()+1);
        if(checkMove(board,position,downRight)){moves.add(new ChessMove(position,downRight));}

        ChessPosition leftUp = new ChessPosition(position.getRow()+1, position.getColumn()-2);
        if(checkMove(board,position,leftUp)){moves.add(new ChessMove(position,leftUp));}
        ChessPosition rightUp = new ChessPosition(position.getRow()+1, position.getColumn()+2);
        if(checkMove(board,position,rightUp)){moves.add(new ChessMove(position,rightUp));}
        ChessPosition leftDown = new ChessPosition(position.getRow()-1, position.getColumn()-2);
        if(checkMove(board,position,leftDown)){moves.add(new ChessMove(position,leftDown));}
        ChessPosition rightDown = new ChessPosition(position.getRow()-1, position.getColumn()+2);
        if(checkMove(board,position,rightDown)){moves.add(new ChessMove(position,rightDown));}

        return moves;
    }

    public boolean checkMove(ChessBoard board,ChessPosition position, ChessPosition newPosition){
        if(board.inBounds(position) && board.inBounds(newPosition)){
            ChessPiece piece=board.getPiece(position);
            ChessPiece newPiece=board.getPiece(newPosition);
            return newPiece==null || piece.getTeamColor()!=newPiece.getTeamColor();
        }
        return false;
    }
}
