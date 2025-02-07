package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable{

    private ChessMove lastMove;
    private TeamColor currentTurn;
    private ChessBoard board;
    private boolean[] castlePieceMoved;

    public ChessGame() {
        currentTurn=TeamColor.WHITE;
        board=new ChessBoard();
        board.resetBoard();
        castlePieceMoved= new boolean[]{false, false, false, false, false, false};
        lastMove=null;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn=team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition)==null){return null;}
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor=piece.getTeamColor();
        Collection<ChessMove> confirmedMoves=new ArrayList<>();
        if(piece.getPieceType()==ChessPiece.PieceType.KING){confirmedMoves.addAll(castlingMoves(startPosition));}
        ChessPiece.PieceType lastPieceType=null;
        if(lastMove!=null){lastPieceType = board.getPiece(lastMove.getEndPosition()).getPieceType();}
        if(piece.getPieceType()==ChessPiece.PieceType.PAWN && lastPieceType==ChessPiece.PieceType.PAWN){
            confirmedMoves.add(enPassantMoves(startPosition));
        }
        Collection<ChessMove> moves= piece.pieceMoves(board,startPosition);
        for(ChessMove move: moves) {
            ChessGame tempGame;
            try {
                tempGame = (ChessGame) this.clone();
                tempGame.tryMove(move);
                if(!tempGame.isInCheck(teamColor)){
                    confirmedMoves.add(move);
                }

            } catch (CloneNotSupportedException | InvalidMoveException e) {
                throw new RuntimeException(e);
            }
        }
        return confirmedMoves;
    }

    public Collection<ChessMove> castlingMoves(ChessPosition kingPosition){
        Collection<ChessMove> moves=new ArrayList<>();
        ChessPiece piece=board.getPiece(kingPosition);
        if (piece == null) {return moves;}
        if(isInDanger(kingPosition,piece.getTeamColor())){return moves;}
        if(piece.getTeamColor()==TeamColor.BLACK){
//            Queenside Castling
            ChessPiece rook=board.getPiece(new ChessPosition(8,1));
            boolean queenside=!castlePieceMoved[4] && !castlePieceMoved[3];
            if(queenside && rook!=null && rook.getPieceType()==ChessPiece.PieceType.ROOK && rook.getTeamColor()==piece.getTeamColor()){
                int col;
                for(col=0; col<3; col++){
                    boolean pieceThere=board.getPiece(new ChessPosition(8,4-col))!=null;
                    boolean positionInDanger= col!=2 && isInDanger(new ChessPosition(8,4-col),piece.getTeamColor());
                    if(pieceThere || positionInDanger){break;}
                }
                if(col==3){moves.add(new ChessMove(new ChessPosition(8,5),new ChessPosition(8,3)));}
            }
//            Kingside Castling
            ChessPiece rook2=board.getPiece(new ChessPosition(8,8));
            boolean kingside=!castlePieceMoved[4] && !castlePieceMoved[5];
            if(kingside && rook2!=null && rook2.getPieceType()== ChessPiece.PieceType.ROOK && rook2.getTeamColor()==piece.getTeamColor()){
                int col;
                for(col=0; col<2;col++){
                    boolean pieceThere=board.getPiece(new ChessPosition(8,6+col))!=null;
                    boolean positionInDanger=isInDanger(new ChessPosition(8,6+col),piece.getTeamColor());
                    if(pieceThere || positionInDanger){break;}
                }
                if(col==2){moves.add(new ChessMove(new ChessPosition(8,5),new ChessPosition(8,7)));}
            }
        }
        if(piece.getTeamColor()==TeamColor.WHITE){
            ChessPiece rook=board.getPiece(new ChessPosition(1,1));
            boolean queenside=!castlePieceMoved[1] && !castlePieceMoved[0];
            if(queenside && rook!=null && rook.getPieceType()==ChessPiece.PieceType.ROOK && rook.getTeamColor()==piece.getTeamColor()){
                int col;
                for(col=0; col<3;col++){
                    boolean pieceThere=board.getPiece(new ChessPosition(1,2+col))!=null;
                    boolean positionInDanger=isInDanger(new ChessPosition(1,2+col),piece.getTeamColor());
                    if(pieceThere || positionInDanger){break;}
                }
                if(col==3){moves.add(new ChessMove(new ChessPosition(1,5),new ChessPosition(1,3)));}
            }
            ChessPiece rook2=board.getPiece(new ChessPosition(1,8));
            boolean kingside=!castlePieceMoved[1] && !castlePieceMoved[2];
            if(kingside && rook2!=null && rook2.getPieceType()==ChessPiece.PieceType.ROOK && rook2.getTeamColor()==piece.getTeamColor()){
                int col;
                for(col=0; col<2;col++){
                    boolean pieceThere=board.getPiece(new ChessPosition(1,6+col))!=null;
                    boolean positionInDanger=isInDanger(new ChessPosition(1,6+col),piece.getTeamColor());
                    if(pieceThere || positionInDanger){break;}
                }
                if(col==2){moves.add(new ChessMove(new ChessPosition(1,5),new ChessPosition(1,7)));}
            }
        }
        return moves;
    }

    public ChessMove enPassantMoves(ChessPosition position){
        if(lastMove==null){return null;}
        ChessPiece lastPiece=board.getPiece(lastMove.getEndPosition());
        ChessPiece piece=board.getPiece(position);
        if(lastPiece==null || lastPiece.getPieceType()!= ChessPiece.PieceType.PAWN){return null;}
        if(piece.getTeamColor()!=lastPiece.getTeamColor()){
            if(Math.abs(lastMove.getStartPosition().getRow()-lastMove.getEndPosition().getRow())==2){
                if(position.getRow()==lastMove.getEndPosition().getRow() && Math.abs(position.getColumn()-lastMove.getEndPosition().getColumn())==1){
                    int newColumn = position.getRow()+(lastMove.getStartPosition().getRow()-lastMove.getEndPosition().getRow())/2;
                    return new ChessMove(position,new ChessPosition(newColumn,lastMove.getEndPosition().getColumn()));
                }
            }
        }
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition())==null || currentTurn!=board.getPiece(move.getStartPosition()).getTeamColor()){
            throw new InvalidMoveException("Invalid Move attempted");
        }
        if(!validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException("Invalid Move attempted");
        }
        try {
            ChessPiece piece=board.getPiece(move.getStartPosition());
            if(move.getPromotionPiece()==null){
                int endCol=move.getEndPosition().getColumn();
                int startCol=move.getStartPosition().getColumn();
                if(piece.getPieceType()== ChessPiece.PieceType.PAWN && endCol!=startCol && board.getPiece(move.getEndPosition())==null){
                    board.addPiece(lastMove.getEndPosition(),null);
                }
                board.addPiece(move.getEndPosition(),piece);
                if(piece.getTeamColor()==TeamColor.BLACK && piece.getPieceType()== ChessPiece.PieceType.KING && !castlePieceMoved[4]){
                    castlePieceMoved[4]=true;
                    if((move.getStartPosition().getColumn()-move.getEndPosition().getColumn())==2){
                        board.addPiece(new ChessPosition(8,4),board.getPiece(new ChessPosition(8,1)));
                        board.addPiece(new ChessPosition(8,1),null);
                        castlePieceMoved[3]=true;
                    }
                    if((move.getStartPosition().getColumn()-move.getEndPosition().getColumn())==-2){
                        board.addPiece(new ChessPosition(8,6),board.getPiece(new ChessPosition(8,8)));
                        board.addPiece(new ChessPosition(8,8),null);
                        castlePieceMoved[5]=true;
                    }
                }
                boolean rooksUnmoved = !castlePieceMoved[3] || !castlePieceMoved[5];
                if(piece.getTeamColor()==TeamColor.BLACK && piece.getPieceType()==ChessPiece.PieceType.ROOK && rooksUnmoved){
                    if(move.getStartPosition().equals(new ChessPosition(8,1))){
                        castlePieceMoved[3]=true;
                    }
                    if(move.getStartPosition().equals(new ChessPosition(8,8))){
                        castlePieceMoved[5]=true;
                    }
                }
                if(piece.getTeamColor()==TeamColor.WHITE && piece.getPieceType()== ChessPiece.PieceType.KING && !castlePieceMoved[1]){
                    castlePieceMoved[1]=true;
                    if((move.getStartPosition().getColumn()-move.getEndPosition().getColumn())==2){
                        board.addPiece(new ChessPosition(1,4),board.getPiece(new ChessPosition(1,1)));
                        board.addPiece(new ChessPosition(1,1),null);
                        castlePieceMoved[0]=true;
                    }
                    if((move.getStartPosition().getColumn()-move.getEndPosition().getColumn())==-2){
                        board.addPiece(new ChessPosition(1,6),board.getPiece(new ChessPosition(1,8)));
                        board.addPiece(new ChessPosition(1,8),null);
                        castlePieceMoved[2]=true;
                    }
                }
                boolean whiteRooksUnmoved = (!castlePieceMoved[0] || !castlePieceMoved[2]);
                if(piece.getTeamColor()==TeamColor.WHITE && piece.getPieceType()== ChessPiece.PieceType.ROOK && whiteRooksUnmoved){
                    if(move.getStartPosition().equals(new ChessPosition(1,1))){
                        castlePieceMoved[0]=true;
                    }
                    if(move.getStartPosition().equals(new ChessPosition(1,8))){
                        castlePieceMoved[2]=true;
                    }
                }
            }
            else{
                board.addPiece(move.getEndPosition(),new ChessPiece(piece.getTeamColor(),move.getPromotionPiece()));
            }
            board.addPiece(move.getStartPosition(),null);
            lastMove=move;
            if(currentTurn==TeamColor.WHITE){
                currentTurn=TeamColor.BLACK;
            }else {currentTurn=TeamColor.WHITE;}
        } catch (Exception e) {
            throw new InvalidMoveException(e.getMessage());
        }
    }

    public void tryMove(ChessMove move) throws InvalidMoveException {
        try {
            ChessPiece piece=board.getPiece(move.getStartPosition());
            if(move.getPromotionPiece()==null){
                board.addPiece(move.getEndPosition(),piece);
            }
            else{
                board.addPiece(move.getEndPosition(),new ChessPiece(piece.getTeamColor(),move.getPromotionPiece()));
            }
            board.addPiece(move.getStartPosition(),null);
            if(currentTurn==TeamColor.WHITE){
                currentTurn=TeamColor.BLACK;
            }else {currentTurn=TeamColor.WHITE;}
        } catch (Exception e) {
            throw new InvalidMoveException(e.getMessage());
        }
    }

    public boolean isInDanger(ChessPosition position,ChessGame.TeamColor teamColor) {
        for(int row=1; row<=8; row++){
            for(int col=1; col<=8; col++){
                ChessPiece attackingPiece = board.getPiece(new ChessPosition(row,col));
                if(attackingPiece==null || attackingPiece.getTeamColor()==teamColor){continue;}
                Collection<ChessMove> moves=attackingPiece.pieceMoves(board,new ChessPosition(row,col));
                for(ChessMove move:moves){
                    if(move.getEndPosition().equals(position)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ChessPosition findKing(TeamColor color){
        for(int row=1; row<=8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece=board.getPiece(new ChessPosition(row,col));
                if(piece!= null && piece.getTeamColor()==color && piece.getPieceType()== ChessPiece.PieceType.KING){
                    return new ChessPosition(row,col);
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition=findKing(teamColor);
        return isInDanger(kingPosition,teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){return false;}
        ChessPosition kingPosition=findKing(teamColor);
        ChessPiece king=board.getPiece(kingPosition);
        for(int row=1; row<=8; row++){
            for(int col=1; col<=8; col++){
                ChessPiece defendingPiece = board.getPiece(new ChessPosition(row,col));
                if(defendingPiece==null || defendingPiece.getTeamColor()!=king.getTeamColor()){continue;}
                Collection<ChessMove> moves = validMoves(new ChessPosition(row, col));
                for (ChessMove move : moves) {
                    ChessGame tempGame;
                    try {
                        tempGame = (ChessGame) this.clone();
                        tempGame.makeMove(move);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (!tempGame.isInCheck(teamColor)) {return false;}
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        for(int row=1; row<=8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if(piece!=null && piece.getTeamColor()==teamColor){
                    Collection<ChessMove> moves = validMoves(new ChessPosition(row,col));
                    if(!moves.isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        try {
            this.board= (ChessBoard) board.clone();
            castlePieceMoved=new boolean[]{false,false,false,false,false,false};
            lastMove=null;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ChessGame clonedGame = (ChessGame) super.clone();
        clonedGame.board= (ChessBoard) this.board.clone();
        clonedGame.currentTurn=currentTurn;
        return clonedGame;
    }

    @Override
    public String toString(){
        return "Turn: " + currentTurn + "\n" +
                board.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        if(obj.getClass()!=this.getClass()){
            return false;
        }
        return ((ChessGame) obj).getBoard().equals(board) && ((ChessGame) obj).getTeamTurn()==currentTurn;
    }

    @Override
    public int hashCode(){
        return board.hashCode()*100+currentTurn.hashCode();
    }
}
