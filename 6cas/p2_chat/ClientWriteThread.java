package p2_chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriteThread extends Thread {

    private final PrintWriter toServer;
    private final String  username;
    public ClientWriteThread(String name, Socket socket) throws IOException {
        this.username = name;
        this.toServer = new PrintWriter(socket.getOutputStream(), true);
        // this.toServer.println("nesto bilo sta");  //ovako automatski radi flush
    }


    //impelentiramo sned(cin)
    @Override
    public void run() {
        //pre svega moramo serveru poslati username clienta
        this.toServer.println(this.username);
        try(Scanner sc = new Scanner(System.in)){
            String msg;
            do {
                System.out.printf("[%s]:", this.username);
                //citamo poruku
                msg = sc.nextLine();
                //saljemo serveru
                this.toServer.println(msg);

                //kako se klijent diskonektuje (ako je bye poruka stajemo)
            } while(!msg.equals("bye"));
        }

    }
}
