package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookCalculator extends LinearCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves=new ArrayList<>();
//        Straights
        moves.addAll(linearMoves(board,position,1,0,8));
        moves.addAll(linearMoves(board,position,-1,0,8));
        moves.addAll(linearMoves(board,position,0,1,8));
        moves.addAll(linearMoves(board,position,0,-1,8));

        return moves;
    }
}
