package ui;

import exception.ResponseException;
import model.GameData;
import facade.HttpCommmunicator;

import java.util.Arrays;
import java.util.HashMap;

public class PostloginClient implements Client{

    private State state;
    private final HttpCommmunicator server;

    public PostloginClient(HttpCommmunicator server) {
        this.server = server;
        state=State.SIGNEDIN;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String help() {
        return """
                create <NAME> - to create a game
                list - to list all games
                join <ID> [WHITE|BLACK] - to join a game
                observe <ID> - to spectate a game
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
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> "Quitting";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String create(String[] input) throws ResponseException {
        if(input.length==1) {
            server.createGame(input[0]);
            return "Successfully create a new game named "+input[0];
        }
        throw new ResponseException(400,"Expected: create <NAME>");
    }

    public String list() throws ResponseException {
        if(state==State.SIGNEDIN){
            StringBuilder gamesList= new StringBuilder("Games:");
            HashMap<Integer, GameData> gamesMap= server.listGames();
            gamesMap.forEach((key,value)-> {
                gamesList.append("\n   ").append(key).append(": ").append(value.gameName());
                gamesList.append("\n      ").append("WHITE: ").append(value.whiteUsername() != null ? value.whiteUsername() : "Not claimed");
                gamesList.append("\n      ").append("BLACK: ").append(value.blackUsername() != null ? value.blackUsername() : "Not claimed");
            });
            return gamesList.toString();
        }
        throw new ResponseException(400,"Unauthorized");
    }

    public String join(String[] input) throws ResponseException {

        if(input.length!=2 || isNotInteger(input[0]) || !input[1].equalsIgnoreCase("WHITE") && !input[1].equalsIgnoreCase("BLACK")){
            throw new ResponseException(400,"Expected: join <ID> WHITE|BLACK");
        }
        else if(!server.getGameMap().containsKey(Integer.parseInt(input[0]))){
            throw new ResponseException(400, "Game ID does not exist");
        }
        else if(state==State.SIGNEDIN) {
            try {
                server.joinGame(input[1].toUpperCase(), Integer.parseInt(input[0]));
                state=input[1].equalsIgnoreCase("WHITE")? State.WHITE: State.BLACK;
                return "Successfully joined game " + input[0];
            } catch (ResponseException e) {
                throw new RuntimeException("Failed to claim that color. Color already taken");
            }
        }
        throw new ResponseException(400,"Unauthorized");
    }

    public String observe(String[] input) throws ResponseException {
        if(input.length!=1 || isNotInteger(input[0])){
            throw new ResponseException(400,"Expected: observe <ID>");
        }
        if(state==State.SIGNEDIN) {
            server.observe(Integer.parseInt(input[0]));
            state=State.OBSERVE;
            return "Viewing Game "+server.getGameMap().get(Integer.parseInt(input[0])).gameName();
        }
        throw new ResponseException(400,"Unauthorized");
    }

    public String logout() throws ResponseException {
        if(state==State.SIGNEDIN) {
            server.logoutUser();
            state=State.SIGNEDOUT;
            return "Logged out. Type help for more assistance";
        }
        throw new ResponseException(400,"Unauthorized");
    }

    private boolean isNotInteger(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

}
