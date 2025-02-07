package chess;

import chess.calculators.*;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable{

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor=pieceColor;
        pieceType=type;
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        MovesCalculator movesCalculator=null;
        switch(pieceType){
            case KING -> movesCalculator=new KingCalculator();
            case PAWN -> movesCalculator=new PawnCalculator();
            case ROOK -> movesCalculator=new RookCalculator();
            case QUEEN -> movesCalculator=new QueenCalculator();
            case BISHOP -> movesCalculator=new BishopCalculator();
            case KNIGHT -> movesCalculator=new KnightCalculator();
        }
        return movesCalculator.pieceMoves(board,myPosition);
    }

    @Override
    public boolean equals(Object obj){
        if(obj==null){
            return false;
        }
        if(obj.getClass()!=this.getClass()){
            return false;
        }
        return ((ChessPiece) obj).pieceType==pieceType && ((ChessPiece) obj).pieceColor==pieceColor;
    }

    @Override
    public String toString(){
        if(pieceColor== ChessGame.TeamColor.BLACK){return pieceType.toString().toLowerCase();}
        return pieceType.toString().toUpperCase();
    }

    @Override
    public int hashCode(){
        return pieceType.hashCode()*100+pieceColor.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
