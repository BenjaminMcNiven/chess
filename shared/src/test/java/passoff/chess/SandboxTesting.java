package passoff.chess;

import chess.*;

import java.util.Scanner;

public class SandboxTesting {

    public static void playGame(){
        ChessGame game=new ChessGame();
        Scanner scanner = new Scanner(System.in);
        while(!game.isInCheckmate(ChessGame.TeamColor.BLACK) && !game.isInCheckmate(ChessGame.TeamColor.WHITE) && !game.isInStalemate(ChessGame.TeamColor.BLACK) && !game.isInStalemate(ChessGame.TeamColor.WHITE)){
            System.out.println(game);
            try {
                String moveText= scanner.nextLine();
                ChessMove nextMove=new ChessMove(new ChessPosition(Integer.parseInt(moveText.split(",")[0]),Integer.parseInt(moveText.split(",")[1])),new ChessPosition(Integer.parseInt(moveText.split(",")[2]),Integer.parseInt(moveText.split(",")[3])));
                game.makeMove(nextMove);
            } catch (NumberFormatException | InvalidMoveException e) {}
        }
        if(game.isInCheckmate(ChessGame.TeamColor.BLACK)){
            System.out.println("BLACK is in Checkmate. Victory WHITE");
        }
        else if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
            System.out.println("WHITE is in Checkmate. Victory BLACK");
        }
        else{
            System.out.println("STALEMATE!");
        }
    }

    public static void main(String[] args){
//        playGame();
        ChessGame game=new ChessGame();
        game.setBoard(new ChessBoard());
        ChessBoard board = game.getBoard();
//        board.addPiece(new ChessPosition(8,1),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
//        board.addPiece(new ChessPosition(8,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
//        board.addPiece(new ChessPosition(8,8),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
//        board.addPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
//        board.addPiece(new ChessPosition(2,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
//        board.addPiece(new ChessPosition(7,2),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
//        board.addPiece(new ChessPosition(7,8),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        board.addPiece(new ChessPosition(2,6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(new ChessPosition(4,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        System.out.println(game);
        System.out.println(game.validMoves(new ChessPosition(2,6)));
//        try {
//            game.setTeamTurn(ChessGame.TeamColor.BLACK);
//            game.makeMove(new ChessMove(new ChessPosition(8,5),new ChessPosition(8,3)));
//            System.out.println(game);
//        } catch (InvalidMoveException e) {
//            throw new RuntimeException(e);
//        }

    }

}
