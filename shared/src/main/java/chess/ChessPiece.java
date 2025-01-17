package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece.PieceType type;
    public ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type=type;
        this.color=pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        MoveCalculator calculator;
        switch(this.getPieceType()){
            case PAWN:
                calculator = new chess.PawnCalculator();
                return calculator.pieceMoves(board,myPosition);
            case ROOK:
                calculator = new RookCalculator();
                return calculator.pieceMoves(board,myPosition);
            case KNIGHT:
                calculator = new KnightCalculator();
                return calculator.pieceMoves(board,myPosition);
            case BISHOP:
                calculator = new BishopCalculator();
                return calculator.pieceMoves(board,myPosition);
            case QUEEN:
                calculator = new QueenCalculator();
                return calculator.pieceMoves(board,myPosition);
            case KING:
                calculator = new KingCalculator();
                return calculator.pieceMoves(board,myPosition);
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }

    @Override
    public String toString() {
        if (color==ChessGame.TeamColor.BLACK){
            return String.format("%s",type).toLowerCase();
        }
        return String.format("%s",type);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return type == that.type && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
}
