package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;

import java.util.Scanner;

public class SandboxTesting {

    public static void playGame(){
        ChessGame game=new ChessGame();
        Scanner scanner = new Scanner(System.in);
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;
        while(!game.isInCheckmate(black) && !game.isInCheckmate(white) && !game.isInStalemate(black) && !game.isInStalemate(white)){
            System.out.println(game);
            try {
                String moveText= scanner.nextLine();
                String[] digits=moveText.split(",");
                ChessPosition startPos = new ChessPosition(Integer.parseInt(digits[0]),Integer.parseInt(digits[1]));
                ChessPosition endPos = new ChessPosition(Integer.parseInt(digits[2]),Integer.parseInt(digits[3]));
                ChessMove nextMove=new ChessMove(startPos,endPos);
                game.makeMove(nextMove);
            } catch (NumberFormatException | InvalidMoveException e) {System.out.println("TRY AGAIN");}
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
