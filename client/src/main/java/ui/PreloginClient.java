package ui;

public class PreloginClient implements Client{

    public PreloginClient(String serverURL){

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
