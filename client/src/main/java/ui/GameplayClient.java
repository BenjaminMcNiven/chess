package ui;

import chess.*;
import exception.ResponseException;
import websocket.ServerMessageObserver;
import websocket.messages.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static ui.EscapeSequences.*;

public class GameplayClient implements Client, ServerMessageObserver {

    private State state;
    private WebSocketCommunicator ws;
    private final String authToken;
    private final int gameID;
    private final String url;

    public GameplayClient(String url, State state, String authToken, int gameID) throws ResponseException {
        ws=new WebSocketCommunicator(url,this);
        this.url=url;
        this.state=state;
        this.authToken = authToken;
        this.gameID = gameID;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                leave();
            } catch (Exception _) {}
        }));
    }

    @Override
    public String help() {
        return """
                redraw - Redraw the board
                highlight <ROW> <COL> = to highlight the valid moves from a piece
                leave - to exit the game
                move <ROW1><COL1> <ROW2><COL2> <PROMOTION>- to make a move, ie move a7 a8 QUEEN
                resign - to resign the game
                help - to see possible commands
                quit - to exit""";
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.strip().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "connect"->connect();
                case "redraw" ->redraw();
                case "highlight" -> highlight(params);
                case "move" -> makeMove(params);
                case "resign" ->resign();
                case "leave" -> leave();
                case "quit" -> leave()+"\nQuitting";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public State getState(){
        return state;
    }

    public String draw(ChessGame game, ChessPosition highlightPos) throws ResponseException {
        String header=drawHeaders();
        String drawnBoard=drawBoard(game, highlightPos);
        return header+drawnBoard+(header).replace("\n","")+RESET_TEXT_BOLD_FAINT;
    }

    private String drawBoard(ChessGame game, ChessPosition highlightPos){
        Collection<ChessMove> highlightMoves =game.validMoves(highlightPos);
        ChessBoard board=game.getBoard();
        StringBuilder result = new StringBuilder();
        int reversed = state.equals(State.WHITE) || state.equals(State.OBSERVE) ? 1 : -1;
        for (int row = reversed == 1 ? 8 : 1; row > 0 && row < 9; row = row - reversed) {
            result.append(SET_BG_COLOR_DARK_GREEN + " ").append(row).append(" ");
            for (int col = reversed == 1 ? 1 : 8; col > 0 && col < 9; col = col + reversed) {
                ChessMove newMove=new ChessMove(highlightPos,new ChessPosition(row,col));
                if(highlightMoves!=null && highlightMoves.contains(newMove)){
                    if(board.getPiece(new ChessPosition(row,col))!=null){
                        result.append(SET_BG_COLOR_LIGHT_RED);
                    }
                    else{
                        result.append(SET_BG_COLOR_YELLOW_LIGHT);
                    }
                }
                else if ((col + row) % 2 == 0) {
                    result.append(SET_BG_COLOR_DARK_GREY);
                } else {
                    result.append(SET_BG_COLOR_LIGHT_GREY);
                }
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    result.append("   ");
                } else {
                    if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        result.append(SET_TEXT_COLOR_BLACK);
                    } else {
                        result.append(RESET_TEXT_COLOR);
                    }
                    switch (piece.getPieceType()) {
                        case PAWN:
                            result.append(" P ");
                            break;
                        case KING:
                            result.append(" K ");
                            break;
                        case KNIGHT:
                            result.append(" N ");
                            break;
                        case BISHOP:
                            result.append(" B ");
                            break;
                        case QUEEN:
                            result.append(" Q ");
                            break;
                        case ROOK:
                            result.append(" R ");
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + piece);
                    }
                }
                result.append(RESET_BG_COLOR + RESET_TEXT_COLOR);
            }
            result.append(SET_BG_COLOR_DARK_GREEN + " ").append(row).append(" " + RESET_BG_COLOR + "\n");
        }
        return result.toString();
    }

    private String drawHeaders() {
        if (state.equals(State.WHITE) || state.equals(State.OBSERVE)) {
            return "\n"+SET_TEXT_BOLD+SET_BG_COLOR_DARK_GREEN+RESET_TEXT_COLOR+ "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";
        }
        return "\n"+SET_TEXT_BOLD+SET_BG_COLOR_DARK_GREEN+RESET_TEXT_COLOR + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n";
    }

    public String connect() throws ResponseException {
        ws.connect(authToken, gameID);
        return "";
    }

    public String leave() throws ResponseException {
        if(state!=State.OBSERVE){
            ws.leave(authToken,gameID);
        }
        state = State.SIGNEDIN;
        return "You left the game. Type help for assistance";
    }

    public String resign() throws ResponseException {
        ws.resign(authToken,gameID);
        return "";
    }

    public String redraw() throws ResponseException {
        ws.redraw(authToken,gameID);
        return "";
    }

    public String highlight(String[] params) throws ResponseException {
        if(params.length<1){
            throw new ResponseException(400,"Expected: highlight <COL><ROW>");
        }
        ChessPosition pos=paramToPos(params[0]);
        if(pos==null){
            throw new ResponseException(400,"Expected: highlight <COL><ROW>");
        }
        ws.highlight(authToken,gameID,pos);
        return "";
    }

    private ChessPosition paramToPos(String param) {
        Map<Character, Integer> letterToNumber = Map.of(
                'a', 1, 'b', 2, 'c', 3, 'd', 4,
                'e', 5, 'f', 6, 'g', 7, 'h', 8
        );
        if(param.length()!=2){
            return null;
        }
        if(!letterToNumber.containsKey(param.charAt(0))){
            return null;
        }
        if(param.charAt(1) < '1' || param.charAt(1) > '8'){
            return null;
        }
        return new ChessPosition(Integer.parseInt(String.valueOf(param.charAt(1))),letterToNumber.get(param.charAt(0)));
    }

    public String makeMove(String[] params) throws ResponseException {
        if(params.length<2){
            throw new ResponseException(400,"Expected: move <ROW1><COL1> <ROW2><COL2> <PROMOTION>");
        }
        ChessPosition pos = paramToPos(params[0]);
        ChessPosition pos2 = paramToPos(params[1]);
        if(pos==null || pos2==null){
            throw new ResponseException(400,"Expected: move <ROW1><COL1> <ROW2><COL2> <PROMOTION>");
        }
        ChessPiece.PieceType promotePiece = null;
        if(params.length==3){
            promotePiece= switch (params[2]){
                case "PAWN" -> ChessPiece.PieceType.PAWN;
                case "ROOK" -> ChessPiece.PieceType.ROOK;
                case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
                case "BISHOP" -> ChessPiece.PieceType.BISHOP;
                case "QUEEN" -> ChessPiece.PieceType.QUEEN;
                default -> null;
            };
            if(promotePiece==null){
                throw new ResponseException(400,"Expected: move <COL><ROW> <COL><ROW>");
            }
        }
        ws.makeMove(authToken,gameID, new ChessMove(pos,pos2,promotePiece));
        return "";
    }


    public void notify(ServerMessage message) {
        if(message.getServerMessageType()== ServerMessage.ServerMessageType.LOAD_GAME){
            try {
                System.out.println(draw(((LoadGameMessage)message).getGame(), null));
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        } else if (message.getServerMessageType()== ServerMessage.ServerMessageType.HIGHLIGHT){
            try {
                System.out.println(draw(((HighlightGameMessage)message).getGame(), ((HighlightGameMessage)message).getPos()));
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        } else if (message.getServerMessageType()== ServerMessage.ServerMessageType.NOTIFICATION){
            System.out.println(RESET_TEXT_COLOR+SET_TEXT_COLOR_BLUE+((NotificationMessage)message).getMessage()+RESET_TEXT_COLOR);
        } else if (message.getServerMessageType()== ServerMessage.ServerMessageType.ERROR){
            if(((ErrorMessage)message).getErrorMessage().contains("Timeout")){
                System.out.println(RESET_TEXT_COLOR+SET_TEXT_COLOR_RED+"Timed out, attempting to reconnect"+RESET_TEXT_COLOR);
                try {
                    ws=new WebSocketCommunicator(url,this);
                    System.out.println(RESET_TEXT_COLOR+SET_TEXT_COLOR_RED+"Reconnected."+RESET_TEXT_COLOR);
                } catch (ResponseException e) {
                    throw new RuntimeException(e);
                }
            }
            else{System.out.println(RESET_TEXT_COLOR+SET_TEXT_COLOR_RED+((ErrorMessage)message).getErrorMessage()+RESET_TEXT_COLOR);}

        }
        System.out.print(RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
