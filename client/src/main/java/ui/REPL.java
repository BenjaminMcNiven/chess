package ui;

import facade.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class REPL{

    private Client client;
    private final ServerFacade server;
    private final State obs=State.OBSERVE;
    private final State bl=State.BLACK;
    private final State w=State.WHITE;

    public REPL(String serverUrl) {
        server=new ServerFacade(serverUrl);
        client = new PreloginClient(server);
    }

    public void run() {
        System.out.println(WHITE_QUEEN+" Welcome to the 240 Chess Client: Sign in to start. "+WHITE_QUEEN);
        System.out.println(client.help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("Quitting")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result + RESET_TEXT_COLOR );
                if(client.getState()==State.SIGNEDIN && client.getClass()!=PostloginClient.class){
                    client=new PostloginClient(server);
                }
                else if(client.getState()==State.SIGNEDOUT && client.getClass()!=PreloginClient.class){
                    client=new PreloginClient(server);
                }
                else if((client.getState()==obs ||client.getState()==w ||client.getState()==bl) && client.getClass()!=GameplayClient.class){
                    client=new GameplayClient(server,client.getState());
                    System.out.println(((GameplayClient)client).redraw());
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}