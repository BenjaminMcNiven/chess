package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnCalculator extends MoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        //Black Pawns
        if(board.getPiece(position).getTeamColor()==ChessGame.TeamColor.BLACK){
            System.out.println("TEST TEST");
            ChessPosition down=new ChessPosition(position.getRow()-1,position.getColumn());
            if(board.getPiece(down)==null){
                moves.add(new ChessMove(position,down));
                if(position.getRow()==7) {
                    ChessPosition down2 = new ChessPosition(down.getRow() - 1, down.getColumn());
                    if (board.getPiece(down2) == null) {
                        moves.add(new ChessMove(position, down2));
                    }
                }
            }
            ChessPosition downLeft=new ChessPosition(position.getRow()-1,position.getColumn()-1);
            if(board.getPiece(downLeft)!=null && board.getPiece(downLeft).getTeamColor()== ChessGame.TeamColor.WHITE){
                moves.add(new ChessMove(position,downLeft));
            }
            ChessPosition downRight=new ChessPosition(position.getRow()-1,position.getColumn()+1);
            if(board.getPiece(downRight)!=null && board.getPiece(downRight).getTeamColor()== ChessGame.TeamColor.WHITE){
                moves.add(new ChessMove(position,downRight));
            }
        }
        else {
            ChessPosition up=new ChessPosition(position.getRow()+1,position.getColumn());
            if (board.getPiece(up) == null) {
                moves.add(new ChessMove(position, up));
                if (position.getRow()==2) {
                    ChessPosition up2=new ChessPosition(up.getRow()+1,up.getColumn());
                    if (board.getPiece(up2) == null) {
                        moves.add(new ChessMove(position, up2));
                    }
                }
            }
            ChessPosition upLeft=new ChessPosition(position.getRow()+1,position.getColumn()-1);
            if (board.getPiece(upLeft) != null && board.getPiece(upLeft).getTeamColor()== ChessGame.TeamColor.BLACK) {

                moves.add(new ChessMove(position, upLeft));
            }
            ChessPosition upRight=new ChessPosition(position.getRow()+1,position.getColumn()+1);
            if (board.getPiece(upRight) != null && board.getPiece(upRight).getTeamColor()== ChessGame.TeamColor.BLACK) {
                moves.add(new ChessMove(position, upRight));
            }
        }
        return moves;
    }
}
