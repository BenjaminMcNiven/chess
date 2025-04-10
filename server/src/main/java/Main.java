import exception.ResponseException;
import facade.HttpCommmunicator;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server myServer = new Server();
        myServer.run(8080);
        try {
            String url = "http://localhost:8080";
            HttpCommmunicator connection = new HttpCommmunicator(url);
            connection.clearDatabase();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
}