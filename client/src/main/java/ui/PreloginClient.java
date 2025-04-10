package ui;

import exception.ResponseException;
import model.UserData;
import facade.HttpCommmunicator;

import java.util.Arrays;
import java.util.Scanner;

public class PreloginClient implements Client {

    private State state;
    private final HttpCommmunicator server;

    public PreloginClient(HttpCommmunicator server) {
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
                case "quit" -> "Quitting";
                case "clear" ->clear();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String register(String[] inputs) throws ResponseException {
        if(inputs.length==3) {
            UserData newUser = new UserData(inputs[0], inputs[1], inputs[2]);
            try {
                var resp = server.register(newUser);
                state=State.SIGNEDIN;
                return "You successfully registered "+resp.username()+". Type help for more assistance";
            } catch (ResponseException e) {
                if(e.getMessage().contains("Forbidden")){
                    throw new ResponseException(403,"Username already taken. Try again");
                }
                throw new RuntimeException(e);
            }
        }
        throw new ResponseException(400, "Expected register <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String[] inputs) throws ResponseException {
        if(inputs.length==2) {
            try {
                UserData newUser = new UserData(inputs[0], inputs[1],null);
                var resp = server.loginUser(newUser);
                state=State.SIGNEDIN;
                return "You successfully logged in as "+resp.username()+". Type help for more assistance";
            } catch (ResponseException e) {
                if(e.getMessage().contains("Unauthorized")){
                    throw new ResponseException(403,"Invalid Username and Password. Try again");
                }
                throw new RuntimeException(e);
            }
        }
        throw new ResponseException(400, "Expected login <USERNAME> <PASSWORD>");
    }

    public String clear(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Admin Password: ");
        String password= scanner.nextLine();
        if(password.equals("clearAdminPassword"))
            try {
                server.clearDatabase();
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        return "Cleared";
    }

    public State getState(){
        return state;
    }

}