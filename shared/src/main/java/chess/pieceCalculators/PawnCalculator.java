package chess.pieceCalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnCalculator implements MovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        if(board.inBounds(position)){
            ChessPiece piece=board.getPiece(position);
//          Black Pawns
            if(piece.getTeamColor()== ChessGame.TeamColor.BLACK){
                ChessPosition down=new ChessPosition(position.getRow()-1, position.getColumn());
                if(checkMove(board,position,down)){
                    moves.add(new ChessMove(position,down));
                    if(position.getRow()==7){
                        ChessPosition down2=new ChessPosition(position.getRow()-2, position.getColumn());
                        if(checkMove(board,position,down2)) {moves.add(new ChessMove(position, down2));}
                    }
                }
                ChessPosition downLeft=new ChessPosition(position.getRow()-1, position.getColumn()-1);
                if(checkMove(board,position,downLeft)) {moves.add(new ChessMove(position, downLeft));}
                ChessPosition downRight=new ChessPosition(position.getRow()-1, position.getColumn()+1);
                if(checkMove(board,position,downRight)) {moves.add(new ChessMove(position, downRight));}

                if(position.getRow()==2){return promoteMoves(moves);}
            }
//          White Pawns
            else{
                ChessPosition up=new ChessPosition(position.getRow()+1, position.getColumn());
                if(checkMove(board,position,up)){
                    moves.add(new ChessMove(position,up));
                    if(position.getRow()==2){
                        ChessPosition up2=new ChessPosition(position.getRow()+2, position.getColumn());
                        if(checkMove(board,position,up2)) {moves.add(new ChessMove(position, up2));}
                    }
                }
                ChessPosition upLeft=new ChessPosition(position.getRow()+1, position.getColumn()-1);
                if(checkMove(board,position,upLeft)) {moves.add(new ChessMove(position, upLeft));}
                ChessPosition upRight=new ChessPosition(position.getRow()+1, position.getColumn()+1);
                if(checkMove(board,position,upRight)) {moves.add(new ChessMove(position, upRight));}

                if(position.getRow()==7){return promoteMoves(moves);}
            }
        }
            return moves;
    }

    public boolean checkMove(ChessBoard board, ChessPosition position, ChessPosition newPosition){
        if(board.inBounds(newPosition)){
            ChessPiece piece=board.getPiece(position);
            ChessPiece newPiece=board.getPiece(newPosition);
            if(position.getColumn()-newPosition.getColumn()==0){
                return newPiece==null;
            }
            if(position.getColumn()-newPosition.getColumn()==1 || position.getColumn()-newPosition.getColumn()==-1){
                return newPiece!=null && newPiece.getTeamColor()!=piece.getTeamColor();
            }
            return false;
        }
        return false;
    }

    public Collection<ChessMove> promoteMoves(Collection<ChessMove> moves){
        Collection<ChessMove> promotedMoves=new ArrayList<>();
        for(ChessMove move: moves) {
            for (ChessPiece.PieceType type : ChessPiece.PieceType.values()) {
                if (type == ChessPiece.PieceType.PAWN || type == ChessPiece.PieceType.KING) {
                    continue;
                }
                promotedMoves.add(new ChessMove(move.getStartPosition(),move.getEndPosition(),type));
            }
        }
        return promotedMoves;
    }
}
