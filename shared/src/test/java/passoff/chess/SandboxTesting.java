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
        playGame();
    }

}
