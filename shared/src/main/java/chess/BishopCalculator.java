package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopCalculator extends MoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        //Up Left
        for (int i=1;i<=8;i++){
            ChessPosition newPosition=new ChessPosition(position.getRow()+i,position.getColumn()-i);
            if(board.inBounds(newPosition)){
                if(board.getPiece(newPosition)==null){
                    moves.add(new ChessMove(position,newPosition));
                }
                else{
                    if(board.getPiece(newPosition).getTeamColor()!=board.getPiece(position).getTeamColor()){
                        moves.add(new ChessMove(position,newPosition));
                    }
                    break;
                }
            }else{break;}
        }
        //Up Right
        for (int i=1;i<=8;i++){
            ChessPosition newPosition=new ChessPosition(position.getRow()+i,position.getColumn()+i);
            if(board.inBounds(newPosition)){
                if(board.getPiece(newPosition)==null){
                    moves.add(new ChessMove(position,newPosition));
                }
                else{
                    if(board.getPiece(newPosition).getTeamColor()!=board.getPiece(position).getTeamColor()){
                        moves.add(new ChessMove(position,newPosition));
                    }
                    break;
                }
            }else{break;}
        }
        //Down Left
        for (int i=1;i<=8;i++){
            ChessPosition newPosition=new ChessPosition(position.getRow()-i,position.getColumn()-i);
            if(board.inBounds(newPosition)){
                if(board.getPiece(newPosition)==null){
                    moves.add(new ChessMove(position,newPosition));
                }
                else{
                    if(board.getPiece(newPosition).getTeamColor()!=board.getPiece(position).getTeamColor()){
                        moves.add(new ChessMove(position,newPosition));
                    }
                    break;
                }
            }else{break;}
        }
        //Down Right
        for (int i=1;i<=8;i++){
            ChessPosition newPosition=new ChessPosition(position.getRow()-i,position.getColumn()+i);
            if(board.inBounds(newPosition)){
                if(board.getPiece(newPosition)==null){
                    moves.add(new ChessMove(position,newPosition));
                }
                else{
                    if(board.getPiece(newPosition).getTeamColor()!=board.getPiece(position).getTeamColor()){
                        moves.add(new ChessMove(position,newPosition));
                    }
                    break;
                }
            }else{break;}
        }
        return moves;
    }

}
