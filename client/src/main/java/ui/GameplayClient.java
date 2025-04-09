package ui;

import chess.*;
import exception.ResponseException;
import websocket.ServerMessageObserver;
import websocket.WebSocketCommunicator;
import websocket.messages.*;

import java.util.Arrays;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class GameplayClient implements Client, ServerMessageObserver {

    private State state;
    private final WebSocketCommunicator ws;
    private final String authToken;
    private final int gameID;

    public GameplayClient(String url, State state, String authToken, int gameID) throws ResponseException {
        ws=new WebSocketCommunicator(url,this);
        this.state=state;
        this.authToken = authToken;
        this.gameID = gameID;
    }

    @Override
    public String help() {
        return """
                redraw - Redraw the board
                highlight <ROW> <COL> = to highlight the valid moves from a piece
                leave - to exit the game
                move <ROW1> <COL1> <ROW2> <COL2> - to make a move
                resign - to resign the game
                help - to see possible commands
                quit - to exit""";
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "connect"->connect();
                case "redraw" ->redraw();
                case "highlight" -> highlight(params);
                case "move" -> makeMove(params);
                case "resign" ->resign();
                case "leave" -> leave();
                case "quit" -> leave()+"Quitting";
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
                if ((col + row) % 2 == 0) {
                    result.append(SET_BG_COLOR_DARK_GREY);
                } else {
                    result.append(SET_BG_COLOR_LIGHT_GREY);
                }
                ChessMove newMove=new ChessMove(highlightPos,new ChessPosition(row,col));
                if(highlightMoves!=null && highlightMoves.contains(newMove)){
                    result.append(SET_BG_COLOR_WHITE);
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
        ws.leave(authToken,gameID);
        state = State.SIGNEDIN;
        return "";
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
        ChessPosition pos = new ChessPosition(Integer.parseInt(params[0]),Integer.parseInt(params[1]));
        ws.highlight(authToken,gameID,pos);
        return "";
    }

    public String makeMove(String[] params) throws ResponseException {
        ChessPosition pos = new ChessPosition(Integer.parseInt(params[0]),Integer.parseInt(params[1]));
        ChessPosition pos2 = new ChessPosition(Integer.parseInt(params[2]),Integer.parseInt(params[3]));
        ws.makeMove(authToken,gameID, new ChessMove(pos,pos2));
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
            System.out.println(RESET_TEXT_COLOR+((NotificationMessage)message).getMessage());
        } else if (message.getServerMessageType()== ServerMessage.ServerMessageType.ERROR){
            System.out.println(RESET_TEXT_COLOR+SET_BG_COLOR_RED+((ErrorMessage)message).getErrorMessage()+RESET_TEXT_COLOR);
        }
        System.out.print(RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
