package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable{

    private ChessPiece[][] board;

    public ChessBoard() {
        board=new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if(inBounds(position)){
            board[8-position.getRow()][position.getColumn()-1]=piece;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if(inBounds(position)){
            return board[8-position.getRow()][position.getColumn()-1];
        }
        return null;
    }

    public boolean inBounds(ChessPosition position){
        return 1<=position.getRow() && position.getRow()<=8 && 1<=position.getColumn() && position.getColumn()<=8;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board=new ChessPiece[8][8];
//        BLACK Pieces
        addPiece(new ChessPosition(8,1),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,8),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,2),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,7),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,3),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,6),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        for(int col=1; col<=8; col++){
            addPiece(new ChessPosition(7,col),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
//        WHITE Pieces
        addPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,2),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,7),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,3),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,6),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        for(int col=1; col<=8; col++){
            addPiece(new ChessPosition(2,col),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        if(obj.getClass()!=this.getClass()){
            return false;
        }
        for(int row=1; row<=8; row++){
            for(int col=1; col<=8; col++){
                ChessPiece piece = getPiece(new ChessPosition(row,col));
                ChessPiece otherPiece=((ChessBoard) obj).getPiece(new ChessPosition(row,col));
                if(piece==null){
                    if(otherPiece==null){
                        continue;
                    }
                    return false;
                }
                else if(!piece.equals(otherPiece)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString(){
        StringBuilder result=new StringBuilder();
        for(ChessPiece[] row:board){
            result.append("| ");
            for(ChessPiece piece:row){
                result.append(piece).append(" | ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChessBoard clonedBoard = (ChessBoard) super.clone();
        for(int row=0; row<=8; row++){
            for(int col=1; col<=8; col++){
                ChessPiece newPiece= (ChessPiece) getPiece(new ChessPosition(row,col)).clone();
                clonedBoard.addPiece(new ChessPosition(row,col), newPiece);
            }
        }
        
        return clonedBoard;
    }

}
