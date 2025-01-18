package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightCalculator extends MoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        //Up Left
        ChessPosition uL=new ChessPosition(position.getRow()+2,position.getColumn()-1);
        if(board.inBounds(uL) && (board.getPiece(uL)==null || board.getPiece(uL).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,uL));
        }
        //Up Right
        ChessPosition uR=new ChessPosition(position.getRow()+2,position.getColumn()+1);
        if(board.inBounds(uR) && (board.getPiece(uR)==null || board.getPiece(uR).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,uR));
        }
        //Down Left
        ChessPosition dL=new ChessPosition(position.getRow()-2,position.getColumn()-1);
        if(board.inBounds(dL) && (board.getPiece(dL)==null || board.getPiece(dL).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,dL));
        }
        //Down Right
        ChessPosition dR=new ChessPosition(position.getRow()-2,position.getColumn()+1);
        if(board.inBounds(dR) && (board.getPiece(dR)==null || board.getPiece(dR).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,dR));
        }
        //Left Up
        ChessPosition lU=new ChessPosition(position.getRow()+1,position.getColumn()-2);
        if(board.inBounds(lU) && (board.getPiece(lU)==null || board.getPiece(lU).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,lU));
        }
        //Left Down
        ChessPosition lD=new ChessPosition(position.getRow()-1,position.getColumn()-2);
        if(board.inBounds(lD) && (board.getPiece(lD)==null || board.getPiece(lD).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,lD));
        }
        //Right Up
        ChessPosition rU=new ChessPosition(position.getRow()+1,position.getColumn()+2);
        if(board.inBounds(rU) && (board.getPiece(rU)==null || board.getPiece(rU).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,rU));
        }
        //Right Down
        ChessPosition rD=new ChessPosition(position.getRow()-1,position.getColumn()+2);
        if(board.inBounds(rD) && (board.getPiece(rD)==null || board.getPiece(rD).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,rD));
        }
        return moves;
    }
}

