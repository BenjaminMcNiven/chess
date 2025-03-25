package ui;

import java.util.Arrays;

import exception.ResponseException;
import model.UserData;
import server.ServerFacade;

public class PreloginClient implements Client {

    private State state;
    private final ServerFacade server;

    public PreloginClient(ServerFacade server) {
        this.server = server;
        state=State.SIGNEDOUT;
    }

    @Override
    public String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                help - to see possible commands
                quit - to exit""";
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String register(String[] inputs) throws ResponseException {
        if(inputs.length==3) {
            UserData newUser = new UserData(inputs[0], inputs[1], inputs[2]);
            var resp = server.register(newUser);
            state=State.SIGNEDIN;
            return "You successfully registered "+resp.username()+". Type help for more assistance";
        }
        throw new ResponseException(400, "Expected register <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String[] inputs) throws ResponseException {
        if(inputs.length==2) {
            UserData newUser = new UserData(inputs[0], inputs[1],null);
            var resp = server.loginUser(newUser);
            state=State.SIGNEDIN;
            return "You successfully logged in as "+resp.username()+". Type help for more assistance";
        }
        throw new ResponseException(400, "Expected login <USERNAME> <PASSWORD>");
    }

    public State getState(){
        return state;
    }

}