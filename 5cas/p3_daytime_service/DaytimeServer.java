package p3_daytime_service;


//server ce da opsluzuje trenutno vreme
//koji god klijent da trazi konekciju sa serverom dobice trenutno vreme

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DaytimeServer {

    //treba nam port na koji ce server da slusa a koji ce i klijent da koristi

    public static  final  int PORT = 12345;

    public static void main(String[] args) {

        //  CLIENT ----- SYN ----> server  (soket)
        //client <----- ACK ---- SERVERT
        // CLIENT ----- ACK -----> server

        // formirmao komunikaciju  u tri korakla i isto tako ih raskidamo

        try(ServerSocket server = new ServerSocket(PORT)){

            System.err.println("Server bound to port: " + PORT);




            while(true) {


                System.err.println("Listening for clients...");

                //TODO miltithread?


                //DA BI IMALI STREAM DO KLIJENTA DA BI MUJ POSLALI VREME MORAMO PRVO DOBITI KLIJENTA, A TO SE RADI SA LISTEN (U JAVI SA ACCEPT)
                try (Socket client = server.accept()) {
                    System.err.println("Accepted client!");

                    //TODO properly dispose stream


                    //POSTO SERVER SAMO PISE KLIJENTU, NISTA NE CITA OD KLIJNETA ZATO NAM NE TREBA INPUTSTREAM
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(client.getOutputStream())  );

                    //trenutno vreme
                    String now = new Date().toString();


                    //write - daje samo bajtovi da se ispisu
                    out.write(now);
                    out.newLine();
                    out.flush();

                    //ovaj catch sluzi ovde da ako sa klijentom immao problem resmo taj problem zatvoirmo soket i idemo dalje na ledeceg klijenta, ne stopiramo citav server
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                System.err.println("Client serverd!");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }



    //TERMINAL 1:  ncat -l 12345   (server)
    //TERMINAK 2: ncat localhost 12345


}
