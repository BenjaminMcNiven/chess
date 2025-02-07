package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class LinearCalculator implements MovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        return new ArrayList<>(linearMoves(board, position, 0, 0, 0));
    }

    public Collection<ChessMove> linearMoves(ChessBoard board, ChessPosition position, int row,int col, int stop) {
        Collection<ChessMove> moves=new ArrayList<>();
        for(int scroll=1; scroll<=8 && scroll<=stop; scroll++){
            ChessPosition newPosition=new ChessPosition(position.getRow()+(scroll*row),position.getColumn()+(scroll*col));
            if(board.inBounds(newPosition)){
                ChessPiece piece=board.getPiece(position);
                ChessPiece newPiece=board.getPiece(newPosition);
                if(newPiece==null){
                    moves.add(new ChessMove(position,newPosition));
                }
                else if(newPiece.getTeamColor()!=piece.getTeamColor()){
                    moves.add(new ChessMove(position,newPosition));
                    break;
                }
                else {break;}
            }
            else {break;}
        }
        return moves;
    }
}
