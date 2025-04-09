package ui;

import chess.*;
import exception.ResponseException;
import websocket.ServerMessageObserver;
import websocket.WebSocketCommunicator;
import websocket.messages.*;

import java.util.Arrays;

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
                exit = to leave the game
                logout - to return to login
                help - to see possible commands
                quit - to exit""";
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "connect"->connect();
                case "redraw" ->redraw();
                case "highlight" -> highlight(params);
                case "move" -> makeMove(params);
                case "resign" ->resign();
                case "leave" -> leave();
                case "quit" -> {
                    leave();
                    return "Quitting";
                }
                default -> {
                    return help();
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }

    @Override
    public State getState(){
        return state;
    }

    public String draw(ChessBoard board, ChessPosition highlightPos) throws ResponseException {
        String header=drawHeaders();
        String drawnBoard=drawBoard(board);
        return header+drawnBoard+header.replace("\n","")+RESET_TEXT_BOLD_FAINT;
    }
    // TODO: Add highlight feature
    private String drawBoard(ChessBoard board){
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
            return SET_TEXT_BOLD+SET_BG_COLOR_DARK_GREEN + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";
        }
        return SET_TEXT_BOLD+SET_BG_COLOR_DARK_GREEN + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n";
    }

    public void connect() throws ResponseException {
        ws.connect(authToken, gameID);
    }

    public void leave() throws ResponseException {
        ws.leave(authToken,gameID);
        state = State.SIGNEDIN;
    }

    public void resign() throws ResponseException {
        ws.resign(authToken,gameID);
    }

    public void redraw() throws ResponseException {
        ws.redraw(authToken,gameID);
    }

    public void highlight(String[] params) throws ResponseException {
        ChessPosition pos = new ChessPosition(Integer.parseInt(params[0]),Integer.parseInt(params[1]));
        ws.highlight(authToken,gameID,pos);
    }

    public void makeMove(String[] params) throws ResponseException {
        ChessPosition pos = new ChessPosition(Integer.parseInt(params[0]),Integer.parseInt(params[1]));
        ChessPosition pos2 = new ChessPosition(Integer.parseInt(params[2]),Integer.parseInt(params[3]));
        ws.makeMove(authToken,gameID, new ChessMove(pos,pos2));
    }


    public void notify(ServerMessage message) {
        if(message.getServerMessageType()== ServerMessage.ServerMessageType.LOAD_GAME){
            try {
                System.out.println(draw(((LoadGameMessage)message).getGame().getBoard(), null));
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        } else if (message.getServerMessageType()== ServerMessage.ServerMessageType.HIGHLIGHT){
            try {
                System.out.println(draw(((HighlightGameMessage)message).getGame().getBoard(), ((HighlightGameMessage)message).getPos()));
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        } else if (message.getServerMessageType()== ServerMessage.ServerMessageType.NOTIFICATION){
            System.out.println(RESET_TEXT_COLOR+((NotificationMessage)message).getMessage());
        } else if (message.getServerMessageType()== ServerMessage.ServerMessageType.ERROR){
            System.out.println(RESET_TEXT_COLOR+SET_BG_COLOR_RED+((ErrorMessage)message).getErrorMessage()+RESET_TEXT_COLOR);
        }
    }
}
