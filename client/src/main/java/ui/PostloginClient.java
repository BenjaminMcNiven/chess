package ui;

import exception.ResponseException;
import model.GameData;
import server.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;

public class PostloginClient implements Client{

    private State state;
    private final ServerFacade server;

    public PostloginClient(ServerFacade server) {
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
            gamesMap.forEach((key,value)->
                gamesList.append("\n   ").append(key).append(": ").append(value.gameName())
            );
            return gamesList.toString();
        }
        throw new ResponseException(400,"Unauthorized");
    }

    public String join(String[] input) throws ResponseException {
        if(state==State.SIGNEDIN) {
            server.joinGame(input[0], Integer.parseInt(input[1]));
            state=State.INGAME;
            return "Successfully joined game " + input[0];
        }
        throw new ResponseException(400,"Unauthorized");
    }

    public String observe(String[] input) throws ResponseException {
        if(state==State.SIGNEDIN) {
//        Insert logic to switch to gameplay client/drawBoard interface
            state=State.OBSERVE;
            return input[0];
        }
        throw new ResponseException(400,"Unauthorized");
    }

    public String logout() throws ResponseException {
        if(state==State.SIGNEDIN) {
            server.logoutUser();
            state=State.SIGNEDOUT;
            return "Logged out";
        }
        throw new ResponseException(400,"Unauthorized");
    }

}
