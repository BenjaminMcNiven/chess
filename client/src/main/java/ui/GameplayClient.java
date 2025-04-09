package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import messages.LoadGameMessage;
import messages.ServerMessage;
import websocket.ServerMessageObserver;
import websocket.WebSocketCommunicator;

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
            return switch (cmd) {
//                case "redraw" -> redraw();
//                case "exit" -> exit();
//                case "highlight" -> highlight();
//                case "move" -> makeMove();
//                case "resign" -> resign();
                case "leave" -> leave();
                case "quit" -> "Quitting";
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

    public String draw(ChessBoard board) throws ResponseException {
        String header=drawHeaders();
        String drawnBoard=drawBoard(board);
        return header+drawnBoard+header.replace("\n","")+RESET_TEXT_BOLD_FAINT;
    }
    // TODO: Add highlight feature
    private String drawBoard(ChessBoard board) throws ResponseException {
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
        ws.connect(authToken, gameID);;
    }

    public String leave() throws ResponseException {
        ws.leave(authToken,gameID);
        state = State.SIGNEDOUT;
        return "Logged out. Type help for more assistance";
    }

    public String exit(){
        state = State.SIGNEDIN;
        return "Exited Game. Type help for more assistance";
    }

    @Override
    public void notify(ServerMessage message) {
        if(message.getServerMessageType()== ServerMessage.ServerMessageType.LOAD_GAME){
            ChessBoard board=((LoadGameMessage)message).getGame().getBoard();
        }
    }
}
