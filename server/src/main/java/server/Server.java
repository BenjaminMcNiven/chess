package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import model.UserData;
import spark.*;
import service.*;

import java.util.Map;
import java.util.Objects;

public class Server {

    public UserDAO userDAO=new MemoryUserDAO();
    public AuthDAO authDAO=new MemoryAuthDAO();
    public GameDAO gameDAO=new MemoryGameDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.
        createRoutes();
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void createRoutes(){
        Spark.before((req, res) -> {
            String path = req.pathInfo();
            if (!path.equals("/db") && !path.equals("/user") && !(path.equals("/session") && req.requestMethod().equals("POST"))) {
                if (!authenticated(req.headers("Authorization"))) {
                    Spark.halt(401, new Gson().toJson(Map.of("message", "Error: unauthorized")));
                }
            }
        });

        Spark.delete("/db", this::clearDatabase);
        Spark.post("/user",this::registerUser);
        Spark.post("/session",this::loginUser);
        Spark.delete("/session",this::logoutUser);
        Spark.get("/game",this::listGames);
        Spark.post("/game",this::createGame);
        Spark.put("/game",this::joinGame);
    }

    public boolean isNullOrEmpty(Object obj){
        if(obj==null){return true;}
        if(obj.getClass()== String.class){
            return ((String) obj).isEmpty();
        }
        return false;
    }

    public boolean authenticated(String authToken){
        AuthenticateService authenticateService=new AuthenticateService(authDAO);
        if(authToken==null || authToken.isEmpty()){
            return false;
        }
        return authenticateService.authenticated(authToken);
    }

    private Object clearDatabase(Request req,Response res){
        ClearDatabaseService clearDatabaseService=new ClearDatabaseService(userDAO,authDAO,gameDAO);
        try{
            clearDatabaseService.clear();
            res.body("");
            return res.body();
        }catch (RuntimeException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: "+e.getMessage()));
        }
    }
    private Object registerUser(Request req,Response res){
        var request = new Gson().fromJson(req.body(), UserData.class);
        if(isNullOrEmpty(request.username()) || isNullOrEmpty(request.password()) || isNullOrEmpty(request.email())){
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        try{
            RegisterService service=new RegisterService(userDAO,authDAO);
            return new Gson().toJson(service.register(request));
        }
        catch(DataAccessException e){
            res.status(403);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: "+e.getMessage()));
        }
    }
    private Object loginUser(Request req,Response res){
        var request = new Gson().fromJson(req.body(), UserData.class);
        if(isNullOrEmpty(request.username()) || isNullOrEmpty(request.password())){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        try{
            LoginService service=new LoginService(userDAO,authDAO);
            return new Gson().toJson(service.login(request));
        }
        catch(DataAccessException e){
            res.status(401);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: "+e.getMessage()));
        }
    }
    private Object logoutUser(Request req,Response res){
        String authToken = req.headers("Authorization");
        if(isNullOrEmpty(authToken)){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        try{
            LogoutService service=new LogoutService(authDAO);
            service.logout(authToken);
            res.body("");
            return res.body();
        } catch (RuntimeException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: "+e.getMessage()));
        }
    }
    private Object listGames(Request req,Response res){
        ListGamesService service=new ListGamesService(gameDAO);
        try{
            return new Gson().toJson(Map.of("games", service.listGames()));
        } catch (RuntimeException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: "+e.getMessage()));
        }
    }
    private Object createGame(Request req,Response res){
        var request = new Gson().fromJson(req.body(), GameData.class);
        if(isNullOrEmpty(request.gameName())){
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        try{
            CreateGameService service=new CreateGameService(authDAO,gameDAO);
            return new Gson().toJson(Map.of("gameID",service.createGame(request.gameName())));
        } catch (RuntimeException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: "+e.getMessage()));
        }
    }
    private Object joinGame(Request req,Response res){
        var request = new Gson().fromJson(req.body(), JoinGameRequest.class);
        String authToken=req.headers("Authorization");
        if(isNullOrEmpty(request.playerColor()) || isNullOrEmpty(request.gameID())){
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        if(!Objects.equals(request.playerColor(), "WHITE") && !Objects.equals(request.playerColor(), "BLACK")){
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        try{
            JoinGameService service=new JoinGameService(authDAO,gameDAO);
            service.joinGame(authToken,request);
            res.body("");
            return res.body();
        }catch (DataAccessException e){
            res.status(403);
            return new Gson().toJson(Map.of("message", "Error: "+e.getMessage()));
        }
        catch (RuntimeException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: "+e.getMessage()));
        }
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
