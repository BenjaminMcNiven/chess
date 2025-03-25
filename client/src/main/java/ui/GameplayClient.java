package ui;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class GameplayClient implements Client{

    private State state;
    private final ServerFacade server;
    private final int gameID;

    public GameplayClient(ServerFacade server, int GameID) {
        this.server = server;
        state=State.INGAME;
        gameID=GameID;
    }

    @Override
    public String help() {
        return """
                redraw - Redraw the board
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
            return switch (cmd) {
                case "redraw" -> redraw();
                case "logout" -> logout();
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

    public String redraw() throws ResponseException {

    }

    public String logout() throws ResponseException {
        if(state==State.INGAME) {
            server.logoutUser();
            state=State.SIGNEDOUT;
            return "Logged out. Type help for more assistance";
        }
        throw new ResponseException(400,"Unauthorized");
    }

}
