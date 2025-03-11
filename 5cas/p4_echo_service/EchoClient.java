package p4_echo_service;

//OPIS PROGRAMA
// CLIENT CE A SE POVEZE I STA GOD DA KLIJENT POSALJE SERVER CE TO DA VIDI I DA VRATI NAZAD ISTU STVAR
// SVE DOK KLIENT NE UNESE 'exit' kad se unese exit prestaje se sa radom

import javax.imageio.IIOException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class EchoClient {

    public static void main(String[] args) {
        String host = "localhost";

        System.err.println("Connecting to " + host);
        try(Socket client = new Socket(host, EchoServer.PORT);
            //SA ULAZA UCITAVAMO NISKE I TO SALJEMO SERVERU  STO MOZE DA BUDE SCANNER ALI
            // CEMO KORISTITI networkIn posto koristimo readere i posto je to bas komunikacij
            // apreko mreza

            //citanje odgovora sa server
            BufferedReader networkIn = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream(),
                            //ako stavim oovde utf_8 moramo i serveru(handleru) da kazemo da se koristi utf_8
                            StandardCharsets.UTF_8
                    )
            );

            //slanje poruke serveru

            BufferedWriter networkOut = new BufferedWriter(
                    new OutputStreamWriter(
                            client.getOutputStream(),
                            StandardCharsets.UTF_8
                    )
            );

            //citanje unosa korisnika sa tastature
            BufferedReader consoleIn = new BufferedReader(
                    new InputStreamReader(
                            System.in
                    )
            )
        ) {
            System.err.println("Connected!");

            //opet petlja jer klijetn isto radi ucitava liniju po liniju

            while (true){

                //ucitavanje sa ulaza
                String line = consoleIn.readLine().trim();
                if(line.equalsIgnoreCase("exit"))
                    break;

                //ako nije break saljemo serveru
                networkOut.write(line);
                networkOut.newLine();
                networkOut.flush();

                //citanje odgovora od servera
                /*
                 networkIn.readLine() – Čita odgovor koji server šalje (pošto je server
                 implementirao echo, to će biti ista poruka koju je klijent poslao).
                 */
                String  echo  = networkIn.readLine();
                System.out.println(echo);

            }






        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}