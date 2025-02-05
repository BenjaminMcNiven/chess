package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition start,end;
    private final ChessPiece.PieceType promotion;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        start=startPosition;
        end=endPosition;
        promotion=promotionPiece;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        start=startPosition;
        end=endPosition;
        promotion=null;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotion;
    }

    @Override
    public boolean equals(Object obj){
        if(obj==null){
            return false;
        }
        if(obj.getClass()!=this.getClass()){
            return false;
        }
        return ((ChessMove) obj).start.equals(start) && ((ChessMove) obj).end.equals(end) && promotion==((ChessMove) obj).promotion;
    }

    @Override
    public String toString(){
        String result=start+" -> "+end;
        if(promotion==null){return result;}
        return result+": "+promotion;
    }

    @Override
    public int hashCode(){
        if(promotion!=null){return start.hashCode()*end.hashCode()^promotion.hashCode();}
        return start.hashCode()*end.hashCode();
    }
}
