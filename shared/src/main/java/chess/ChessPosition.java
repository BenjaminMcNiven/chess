package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row,col;

    public ChessPosition(int row, int col) {
    this.row=row;
    this.col=col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object obj){
        if(obj==null){
            return false;
        }
        if(obj.getClass()!=this.getClass()){
            return false;
        }
        return ((ChessPosition) obj).row==this.row && ((ChessPosition) obj).col==this.col;
    }

    @Override
    public String toString(){
        return "("+row+", "+col+")";
    }

    @Override
    public int hashCode(){
        return 100*row + col;
    }
}
