package ui;

public class PreloginClient implements Client{

//    private final ServerFacade server;
    private final String serverURL;

    public PreloginClient(String serverURL){
//        server = new ServerFacade(serverUrl);
        this.serverURL= serverURL;
    }

    @Override
    public String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - to exit
                help - to see possible commands
                """;
    }

    @Override
    public String eval(String input) {
        return "";
    }
}
