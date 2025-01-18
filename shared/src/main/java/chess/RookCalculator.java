package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookCalculator extends MoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        //Horizontal Left moves
        for(int i=position.getColumn()-1; i>=1; i--){
            ChessPosition newPosition = new ChessPosition(position.getRow(),i);
            if(board.inBounds(newPosition) && board.getPiece(newPosition)==null){
                moves.add(new ChessMove(position,newPosition));
            }
            else{
                if(board.getPiece(newPosition).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,newPosition));
                }
                break;
            }
        }
        //Horizontal Right moves
        for(int i=position.getColumn()+1; i<=8; i++){
            ChessPosition newPosition = new ChessPosition(position.getRow(),i);
            if(board.inBounds(newPosition) && board.getPiece(newPosition)==null){
                moves.add(new ChessMove(position,newPosition));
            }
            else{
                if(board.getPiece(newPosition).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,newPosition));
                }
                break;
            }
        }
        //Vertical Down moves
        for(int i=position.getRow()-1; i>=1; i--){
            ChessPosition newPosition = new ChessPosition(i, position.getColumn());
            if(board.inBounds(newPosition) && board.getPiece(newPosition)==null){
                moves.add(new ChessMove(position,newPosition));
            }
            else{
                if(board.getPiece(newPosition).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,newPosition));
                }
                break;
            }
        }
        //Vertical Up moves
        for(int i=position.getRow()+1; i<=8; i++){
            ChessPosition newPosition = new ChessPosition(i, position.getColumn());
            if(board.inBounds(newPosition) && board.getPiece(newPosition)==null){
                moves.add(new ChessMove(position,newPosition));
            }
            else{
                if(board.getPiece(newPosition).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,newPosition));
                }
                break;
            }
        }
        return moves;
    }
}
