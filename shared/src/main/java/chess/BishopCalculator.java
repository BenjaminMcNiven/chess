package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopCalculator extends MoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        //Up Left
        for (int i=1;i<=8;i++){
            ChessPosition new_pos=new ChessPosition(position.getRow()+i,position.getColumn()-i);
            if(board.inBounds(new_pos)){
                if(board.getPiece(new_pos)==null){
                    moves.add(new ChessMove(position,new_pos));
                }
                else{
                    if(board.getPiece(new_pos).getTeamColor()!=board.getPiece(position).getTeamColor()){
                        moves.add(new ChessMove(position,new_pos));
                    }
                    break;
                }
            }else{break;}
        }
        //Up Right
        for (int i=1;i<=8;i++){
            ChessPosition new_pos=new ChessPosition(position.getRow()+i,position.getColumn()+i);
            if(board.inBounds(new_pos)){
                if(board.getPiece(new_pos)==null){
                    moves.add(new ChessMove(position,new_pos));
                }
                else{
                    if(board.getPiece(new_pos).getTeamColor()!=board.getPiece(position).getTeamColor()){
                        moves.add(new ChessMove(position,new_pos));
                    }
                    break;
                }
            }else{break;}
        }
        //Down Left
        for (int i=1;i<=8;i++){
            ChessPosition new_pos=new ChessPosition(position.getRow()-i,position.getColumn()-i);
            if(board.inBounds(new_pos)){
                if(board.getPiece(new_pos)==null){
                    moves.add(new ChessMove(position,new_pos));
                }
                else{
                    if(board.getPiece(new_pos).getTeamColor()!=board.getPiece(position).getTeamColor()){
                        moves.add(new ChessMove(position,new_pos));
                    }
                    break;
                }
            }else{break;}
        }
        //Down Right
        for (int i=1;i<=8;i++){
            ChessPosition new_pos=new ChessPosition(position.getRow()-i,position.getColumn()+i);
            if(board.inBounds(new_pos)){
                if(board.getPiece(new_pos)==null){
                    moves.add(new ChessMove(position,new_pos));
                }
                else{
                    if(board.getPiece(new_pos).getTeamColor()!=board.getPiece(position).getTeamColor()){
                        moves.add(new ChessMove(position,new_pos));
                    }
                    break;
                }
            }else{break;}
        }
        return moves;
    }

}
