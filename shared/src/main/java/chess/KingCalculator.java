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
        ChessPosition uL=new ChessPosition(position.getRow()+1,position.getColumn()-1);
        if(board.inBounds(uL) && (board.getPiece(uL)==null || board.getPiece(uL).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,uL));
        }
        ChessPosition uR=new ChessPosition(position.getRow()+1,position.getColumn()+1);
        if(board.inBounds(uR) && (board.getPiece(uR)==null || board.getPiece(uR).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,uR));
        }
        ChessPosition dL=new ChessPosition(position.getRow()-1,position.getColumn()-1);
        if(board.inBounds(dL) && (board.getPiece(dL)==null || board.getPiece(dL).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,dL));
        }
        ChessPosition dR=new ChessPosition(position.getRow()-1,position.getColumn()+1);
        if(board.inBounds(dR) && (board.getPiece(dR)==null || board.getPiece(dR).getTeamColor()!= board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,dR));
        }
        return moves;
    }
}
