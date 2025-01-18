package chess;
import java.util.Collection;
import java.util.ArrayList;

public class QueenCalculator extends MoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(new RookCalculator().pieceMoves(board,position));
        moves.addAll(new BishopCalculator().pieceMoves(board,position));
        return moves;
    }
}
