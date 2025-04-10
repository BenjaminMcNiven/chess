package facade;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HttpCommmunicator {

    private final String serverUrl;
    private String authToken;
    private HashMap<Integer,GameData> gameMap;
    private Integer activeGame;
    private String username;

    public HttpCommmunicator(String url) {
        serverUrl = url;
    }

    public void clearDatabase() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public AuthData register(UserData newUser) throws ResponseException {
        var path = "/user";
        AuthData newAuth=this.makeRequest("POST", path, newUser, AuthData.class);
        authToken=newAuth.authToken();
        username= newAuth.username();
        return newAuth;
    }

    public AuthData loginUser(UserData loginUser) throws ResponseException {
        var path = "/session";
        AuthData newAuth=this.makeRequest("POST", path, loginUser, AuthData.class);
        authToken=newAuth.authToken();
        username= newAuth.username();
        return newAuth;
    }

    public void logoutUser() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
        authToken=null;
        username=null;
        activeGame=null;
        gameMap=null;
    }

    public HashMap<Integer, GameData> listGames() throws ResponseException {
        if(authToken!=null){
            var path = "/game";
            Type responseType = new TypeToken<Map<String, Collection<GameData>>>() {}.getType();
            Map<String, Collection<GameData>> response = this.makeRequest("GET", path, null, responseType);
            createGameMap((ArrayList<GameData>)response.get("games"));
            activeGame=null;
            return gameMap;
        }
        throw new ResponseException(400, "Unauthorized");
    }

    private void createGameMap(ArrayList<GameData> games){
        if(gameMap!=null){gameMap.clear();}
        else{
            gameMap=new HashMap<>();
        }
        int count=1;
        for(GameData game : games.reversed()){
            gameMap.put(count, game);
            count++;
        }
    }

    public HashMap<Integer,GameData> getGameMap(){
        return gameMap;
    }

    public void observe(int gameID) throws ResponseException {
        if(gameMap==null){
            throw new ResponseException(500, "Games not yet listed! List the games to find a game to join");
        }
        activeGame=gameID;
    }

    public GameData getActiveGame() throws ResponseException {
        if(activeGame!=null){
            return gameMap.get(activeGame);
        }
        throw new ResponseException(400,"No Active Game");
    }

    public String getAuthToken() {
        return authToken;
    }

    public void createGame(String gameName) throws ResponseException {
        if(authToken!=null){
            GameData newGame = new GameData(null,null,null,gameName,null);
            var path = "/game";
            this.makeRequest("POST", path, newGame, null);
        }
        else{
            throw new ResponseException(400, "Unauthorized");
        }
    }

    public void joinGame(String color, int gameID) throws ResponseException {
        if(authToken!=null) {
            if(username.equals(gameMap.get(gameID).blackUsername()) || username.equals(gameMap.get(gameID).whiteUsername())){
                throw new ResponseException(500, "You have already joined that game");
            }
            if(color.equals("BLACK")? username.equals(gameMap.get(gameID).blackUsername()): username.equals(gameMap.get(gameID).whiteUsername())){
                activeGame=gameID;
            }
            else{
                JoinGameRequest newJoin = new JoinGameRequest(color, gameMap.get(gameID).gameID());
                var path = "/game";
                this.makeRequest("PUT", path, newJoin, null);
                activeGame=gameID;
            }
        }
        else{
            throw new ResponseException(400,"Unauthorized");
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Type responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, http.getResponseMessage());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Type responseClass) throws IOException {
        T response = null;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(respBody);
            if (responseClass != null) {
                response = new Gson().fromJson(reader, responseClass);
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}