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

    private TeamColor currentTurn;
    private ChessBoard board;

    public ChessGame() {
        currentTurn=TeamColor.WHITE;
        board=new ChessBoard();
        board.resetBoard();
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


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(!validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException("Invalid Move attempted");
        }
        if(board.getPiece(move.getStartPosition())==null || currentTurn!=board.getPiece(move.getStartPosition()).getTeamColor()){
            throw new InvalidMoveException("Invalid Move attempted");
        }
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

    public boolean isInDanger(ChessPosition position) {
        ChessPiece piece=board.getPiece(position);
        for(int row=1; row<=8; row++){
            for(int col=1; col<=8; col++){
                ChessPiece attackingPiece = board.getPiece(new ChessPosition(row,col));
                if(attackingPiece!=null && attackingPiece.getTeamColor()!=piece.getTeamColor()){
                    Collection<ChessMove> moves=attackingPiece.pieceMoves(board,new ChessPosition(row,col));
                    for(ChessMove move:moves){
                        if(move.getEndPosition().equals(position)){
                            return true;
                        }
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
        return isInDanger(kingPosition);
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
                if(defendingPiece!=null && defendingPiece.getTeamColor()==king.getTeamColor()){
                    Collection<ChessMove> moves=validMoves(new ChessPosition(row,col));
                    for(ChessMove move:moves){
                        try {
                            ChessGame tempGame = (ChessGame) this.clone();
                            tempGame.makeMove(move);
                            if(!tempGame.isInCheck(teamColor)){
                                return false;
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
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
