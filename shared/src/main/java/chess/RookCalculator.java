package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookCalculator extends MoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        //Horizontal Left moves
        for(int i=position.getColumn()-1; i>=1; i--){
            if(board.getPiece(new ChessPosition(position.getRow(),i))==null){
                moves.add(new ChessMove(position,new ChessPosition(position.getRow(),i)));
            }
            else{
                if(board.getPiece(new ChessPosition(position.getRow(),i)).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,new ChessPosition(position.getRow(),i)));
                }
                break;
            }
        }
        //Horizontal Right moves
        for(int i=position.getColumn()+1; i<=8; i++){
            if(board.getPiece(new ChessPosition(position.getRow(),i))==null){
                moves.add(new ChessMove(position,new ChessPosition(position.getRow(),i)));
            }
            else{
                if(board.getPiece(new ChessPosition(position.getRow(),i)).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,new ChessPosition(position.getRow(),i)));
                }
                break;
            }
        }
        //Vertical Down moves
        for(int i=position.getRow()-1; i>=1; i--){
            if(board.getPiece(new ChessPosition(i, position.getColumn()))==null){
                moves.add(new ChessMove(position,new ChessPosition(i, position.getColumn())));
            }
            else{
                if(board.getPiece(new ChessPosition(i,position.getColumn())).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,new ChessPosition(i, position.getColumn())));
                }
                break;
            }
        }
        //Vertical Up moves
        for(int i=position.getRow()+1; i<=8; i++){
            if(board.getPiece(new ChessPosition(i, position.getColumn()))==null){
                moves.add(new ChessMove(position,new ChessPosition(i,position.getColumn())));
            }
            else{
                if(board.getPiece(new ChessPosition(i,position.getColumn())).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,new ChessPosition(i, position.getColumn())));
                }
                break;
            }
        }
        return moves;
    }
}
