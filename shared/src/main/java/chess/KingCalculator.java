package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KingCalculator extends MoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        //Up,Down,Left,Right
        ChessPosition up=new ChessPosition(position.getRow()+1,position.getColumn());
        if(board.inBounds(up) && (board.getPiece(up)==null || board.getPiece(up).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,up));
        }
        ChessPosition down=new ChessPosition(position.getRow()-1,position.getColumn());
        if(board.inBounds(down) && (board.getPiece(down)==null || board.getPiece(down).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,down));
        }
        ChessPosition left=new ChessPosition(position.getRow(),position.getColumn()-1);
        if(board.inBounds(left) && (board.getPiece(left)==null || board.getPiece(left).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,left));
        }
        ChessPosition right=new ChessPosition(position.getRow(),position.getColumn()+1);
        if(board.inBounds(right) && (board.getPiece(right)==null || board.getPiece(right).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,right));
        }
        //Diagonals
        ChessPosition upLeft=new ChessPosition(position.getRow()+1,position.getColumn()-1);
        if(board.inBounds(upLeft) && (board.getPiece(upLeft)==null || board.getPiece(upLeft).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,upLeft));
        }
        ChessPosition upRight=new ChessPosition(position.getRow()+1,position.getColumn()+1);
        if(board.inBounds(upRight) && (board.getPiece(upRight)==null || board.getPiece(upRight).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,upRight));
        }
        ChessPosition downLeft=new ChessPosition(position.getRow()-1,position.getColumn()-1);
        if(board.inBounds(downLeft) && (board.getPiece(downLeft)==null || board.getPiece(downLeft).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,downLeft));
        }
        ChessPosition downRight=new ChessPosition(position.getRow()-1,position.getColumn()+1);
        if(board.inBounds(downRight) && (board.getPiece(downRight)==null || board.getPiece(downRight).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,downRight));
        }
        return moves;
    }

}
