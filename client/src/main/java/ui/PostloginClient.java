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
                case "quit" -> "quit";
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

        if(input.length!=2 || !input[1].equals("WHITE") && !input[1].equals("BLACK")){
            throw new ResponseException(400,"Expected: join <ID> WHITE|BLACK");
        }
        else if(!server.getGameMap().containsKey(Integer.parseInt(input[0]))){
            throw new ResponseException(400, "Game ID does not exist");
        }
        else if(state==State.SIGNEDIN) {
            try {
                server.joinGame(input[1], Integer.parseInt(input[0]));
                state=input[1].equals("WHITE")? State.WHITE: State.BLACK;
                return "Successfully joined game " + input[0];
            } catch (ResponseException e) {
                throw new RuntimeException("Failed to claim that color. Color already taken");
            }
        }
        throw new ResponseException(400,"Unauthorized");
    }

    public String observe(String[] input) throws ResponseException {
        if(input.length!=1){
            throw new ResponseException(400,"Expected: observe <ID>");
        }
        if(state==State.SIGNEDIN) {
//        Insert logic to switch to gameplay client/drawBoard interface
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

}
