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

    public Collection<ChessMove> iterateMoves(ChessBoard board, ChessPosition position, int row,int col) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            ChessPosition newPosition = new ChessPosition(position.getRow() + i * row, position.getColumn() + i * col);
            if (board.inBounds(newPosition)) {
                if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(position, newPosition));
                } else {
                    if (board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        moves.add(new ChessMove(position, newPosition));
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

