package p4_echo_service;

//OPIS PROGRAMA
// CLIENT CE A SE POVEZE I STA GOD DA KLIJENT POSALJE SERVER CE TO DA VIDI I DA VRATI NAZAD ISTU STVAR
// SVE DOK KLIENT NE UNESE 'exit' kad se unese exit prestaje se sa radom

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static final int PORT = 23456;


    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.err.println("Server bound to port: " + PORT);
            //brojac klijenta
            int n = 0;

            while (true) {
                System.err.println("Listening for clients....");
                Socket client = server.accept();
                //kada kreiramo klijenta uvecamo brojac
                n++;
                System.err.println("Accepted  client!");

                //pokrece nit za svakog kreiranog klijenta
                new Thread((Runnable) new ClientHandlerRunnable(client)).start();


                System.out.println("Client served!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

//JOIN RADIMO KADA SAM JE BITAN U TRENUTNOM TREDU IZ KOG RADIMO JOIN REZULTAT RADA TREDA


//POKRECEMO U TERMINALU SA: ncat localhost 23456