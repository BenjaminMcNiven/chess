package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenCalculator extends LinearCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves=new ArrayList<>();
        moves.addAll(new RookCalculator().pieceMoves(board,position));
        moves.addAll(new BishopCalculator().pieceMoves(board,position));
        return moves;
    }
}
