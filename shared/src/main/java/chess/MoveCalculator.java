package chess;
import java.util.Collection;
import java.util.ArrayList;

public class MoveCalculator {

    public MoveCalculator(){
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board,ChessPosition position){
        Collection<ChessMove> moves = new ArrayList<>();
            moves.add(new ChessMove(new ChessPosition(0,0),new ChessPosition(0,1)));
        return moves;
    }

}
