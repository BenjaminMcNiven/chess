package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopCalculator extends MoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(iterateMoves(board,position,1,-1));
        moves.addAll(iterateMoves(board,position,1,1));
        moves.addAll(iterateMoves(board,position,-1,-1));
        moves.addAll(iterateMoves(board,position,-1,1));

        return moves;
    }

    public Collection<ChessMove> iterateMoves(ChessBoard board, ChessPosition position, int row, int col) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            ChessPosition newPos = new ChessPosition(position.getRow() + i * row, position.getColumn() + i * col);
            if (board.inBounds(newPos)) {
                if (board.getPiece(newPos) == null) {
                    moves.add(new ChessMove(position, newPos));
                } else {
                    if (board.getPiece(newPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        moves.add(new ChessMove(position, newPos));
                    }
                    return moves;
                }
            } else {
                return moves;
            }
        }
        return moves;
    }
}

