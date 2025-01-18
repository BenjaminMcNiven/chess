package chess;

import java.util.Collection;
import java.util.ArrayList;

public class QueenCalculator extends MoveCalculator{

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
        //Horizontal Left moves
        for(int i=position.getColumn()-1; i>=1; i--){
            ChessPosition new_pos = new ChessPosition(position.getRow(),i);
            if(board.inBounds(new_pos) && board.getPiece(new_pos)==null){
                moves.add(new ChessMove(position,new_pos));
            }
            else{
                if(board.getPiece(new_pos).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,new_pos));
                }
                break;
            }
        }
        //Horizontal Right moves
        for(int i=position.getColumn()+1; i<=8; i++){
            ChessPosition new_pos = new ChessPosition(position.getRow(),i);
            if(board.inBounds(new_pos) && board.getPiece(new_pos)==null){
                moves.add(new ChessMove(position,new_pos));
            }
            else{
                if(board.getPiece(new_pos).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,new_pos));
                }
                break;
            }
        }
        //Vertical Down moves
        for(int i=position.getRow()-1; i>=1; i--){
            ChessPosition new_pos = new ChessPosition(i, position.getColumn());
            if(board.inBounds(new_pos) && board.getPiece(new_pos)==null){
                moves.add(new ChessMove(position,new_pos));
            }
            else{
                if(board.getPiece(new_pos).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,new_pos));
                }
                break;
            }
        }
        //Vertical Up moves
        for(int i=position.getRow()+1; i<=8; i++){
            ChessPosition new_pos = new ChessPosition(i, position.getColumn());
            if(board.inBounds(new_pos) && board.getPiece(new_pos)==null){
                moves.add(new ChessMove(position,new_pos));
            }
            else{
                if(board.getPiece(new_pos).getTeamColor()!=board.getPiece(position).getTeamColor()){
                    moves.add(new ChessMove(position,new_pos));
                }
                break;
            }
        }
        return moves;
    }

}
