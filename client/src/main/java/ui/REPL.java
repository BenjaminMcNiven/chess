package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class REPL{

    private final Client client;

    public REPL(String serverUrl) {
        client = new PreloginClient(serverUrl);
    }

    public void run() {
        System.out.println(WHITE_QUEEN+" Welcome to the 240 Chess Client: Sign in to start. "+WHITE_QUEEN);
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
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