package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightCalculator extends MoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        //Up Left
        ChessPosition upLeft=new ChessPosition(position.getRow()+2,position.getColumn()-1);
        if(board.inBounds(upLeft) && (board.getPiece(upLeft)==null || board.getPiece(upLeft).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,upLeft));
        }
        //Up Right
        ChessPosition upRight=new ChessPosition(position.getRow()+2,position.getColumn()+1);
        if(board.inBounds(upRight) && (board.getPiece(upRight)==null || board.getPiece(upRight).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,upRight));
        }
        //Down Left
        ChessPosition downLeft=new ChessPosition(position.getRow()-2,position.getColumn()-1);
        if(board.inBounds(downLeft) && (board.getPiece(downLeft)==null || board.getPiece(downLeft).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,downLeft));
        }
        //Down Right
        ChessPosition downRight=new ChessPosition(position.getRow()-2,position.getColumn()+1);
        if(board.inBounds(downRight) && (board.getPiece(downRight)==null || board.getPiece(downRight).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,downRight));
        }
        //Left Up
        ChessPosition leftUp=new ChessPosition(position.getRow()+1,position.getColumn()-2);
        if(board.inBounds(leftUp) && (board.getPiece(leftUp)==null || board.getPiece(leftUp).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,leftUp));
        }
        //Left Down
        ChessPosition leftDown=new ChessPosition(position.getRow()-1,position.getColumn()-2);
        if(board.inBounds(leftDown) && (board.getPiece(leftDown)==null || board.getPiece(leftDown).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,leftDown));
        }
        //Right Up
        ChessPosition rightUp=new ChessPosition(position.getRow()+1,position.getColumn()+2);
        if(board.inBounds(rightUp) && (board.getPiece(rightUp)==null || board.getPiece(rightUp).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,rightUp));
        }
        //Right Down
        ChessPosition rightDown=new ChessPosition(position.getRow()-1,position.getColumn()+2);
        if(board.inBounds(rightDown) && (board.getPiece(rightDown)==null || board.getPiece(rightDown).getTeamColor() != board.getPiece(position).getTeamColor())){
            moves.add(new ChessMove(position,rightDown));
        }
        return moves;
    }
}

