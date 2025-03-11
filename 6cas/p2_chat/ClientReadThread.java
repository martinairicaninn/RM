package p2_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReadThread extends Thread {


    //objekat klase BufferedReader, koji se koristi za citanje podataka sa servera preko socket-a
    private BufferedReader fromServer;

    //korisnicko ime klijenta koji se koristi za prikazivanje poruka sa identifikacijom korinika
    private final String username;
    public ClientReadThread(String name, Socket socket) throws IOException {
        this.username = name;
        /*
        Kreiramo BufferedReader koji čita podatke sa servera putem socket-a. Socket omogućava komunikaciju sa
        serverom, a BufferedReader omogućava da čitamo podatke u formi linija.
         */
        this.fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


    //implementiramo (reseve/cout)
    @Override
    public void run() {
        /*
        Petlja while (true) osigurava da thread stalno čita podatke sa servera dok god veza nije prekinuta.
         */
        while ((true)) {

            try {
                //citamo liniju
                String line = this.fromServer.readLine();

                //ako se validno diskonektujemo
                if(line == null){
                    System.err.println("Connection closed");
                    break;
                }
                System.out.println("\r" + line);
                System.out.printf("\r[%s]:", this.username);
            } catch (IOException e) {

                System.err.println("Error reading from server");
                e.printStackTrace();
                //break jer ako smo failovali jendom vrv cemo failovati opet
                break;
            }
        }


    }
}
