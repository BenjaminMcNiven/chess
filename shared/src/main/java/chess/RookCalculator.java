package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookCalculator extends MoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(iterateMovesVertical(board,position,1));
        moves.addAll(iterateMovesVertical(board,position,-1));
        moves.addAll(iterateMovesHorizontal(board,position,1));
        moves.addAll(iterateMovesHorizontal(board,position,-1));

        return moves;
    }

    public Collection<ChessMove> iterateMovesHorizontal(ChessBoard board, ChessPosition position, int direction){
        Collection<ChessMove> moves = new ArrayList<>();
        for(int i=1; i<=8; i++) {
            ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() + (i * direction));
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

    public Collection<ChessMove> iterateMovesVertical(ChessBoard board, ChessPosition position, int direction){
        Collection<ChessMove> moves = new ArrayList<>();
        for(int i=1; i<=8; i++) {
            ChessPosition newPosition = new ChessPosition(position.getRow() + (i * direction), position.getColumn());
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
