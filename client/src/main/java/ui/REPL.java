package ui;

import server.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class REPL{

    private Client client;
    private final ServerFacade server;

    public REPL(String serverUrl) {
        server=new ServerFacade(serverUrl);
        client = new PreloginClient(server);
    }

    public void run() {
        System.out.println(WHITE_QUEEN+" Welcome to the 240 Chess Client: Sign in to start. "+WHITE_QUEEN);
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print(client.help());
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result + RESET_TEXT_COLOR );
                if(client.getState()==State.SIGNEDIN){
                    client=new PostloginClient(server);
                    System.out.println("Welcome to the Logged-In Interface");
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}