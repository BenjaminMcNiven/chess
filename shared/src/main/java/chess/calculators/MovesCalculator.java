package chess.calculators;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;
import java.util.Collection;

public interface MovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}
