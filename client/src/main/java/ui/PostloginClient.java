package ui;

import java.util.Arrays;

public class PostloginClient implements Client{
    @Override
    public String help() {
        return """
                create <NAME> - to create a game
                list - to list all games
                join <ID> [WHITE|BLACK] - to join a game
                observe <ID> - to spectate a game
                logout - to return to login
                quit - to exit
                help - to see possible commands""";
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> quit();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String create(String[] input){
        return "";
    }

    public String list(){
        return "";
    }

    public String join(String[] input){
        return "";
    }

    public String observe(String[] input){
        return "";
    }

    public String logout(){
        return "";
    }

    public String quit(){
        return "";
    }
}
