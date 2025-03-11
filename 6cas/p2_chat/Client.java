package p2_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


//terminal
public class Client {

    public static void main(String[] args) throws IOException {
        String hostname = "localhost";
        int port = Server.PORT;
        Client client = new Client("localhost", Server.PORT );
        System.err.println("Connecting to: " + hostname + ":" + port);
        client.execute();

    }

    private final String hostname;
    private final int port;

    //korisnicko ime koje ce uneti klijent
    private  String name;

     public Client(String hostname, int port){
            this.hostname = hostname;
            this.port = port;
    }

    //ova metoda pokrece povezivanje sa serverom i komunicira sa njim
    void execute() throws IOException {

           //pre nego sto se povezemo korisnik mora uneti svoje ime

           this.setName();


           try(Socket socket = new Socket(this.hostname, this.port)){
               System.err.println("Connected to chat server @ "  + this.hostname);

               //oravimo dve niti koje su zaduzene za primanje i slanje poruka serveru
                   Thread rt = new ClientReadThread(this.name, socket);
                   Thread wt = new ClientWriteThread(this.name, socket);


                   rt.start();
                   wt.start();

                   /*imamo tri thread-a
                         main (kreira read i write, ali nece nista raditi dok se ovi threadovi ne joinuju,
                               kad se read joinuje onda nastavlja pa opet ceka da se write joinuju pa tek tada
                               nastavlja sa radom)

                            read
                            write
                   */

               //cekamo da se niti zavrse i glavni program ne nastavlja dalje dok se niti ne zavrse
                   rt.join();
                   wt.join();


           } catch (UnknownHostException e) {
               throw new RuntimeException(e);
           } catch (IOException e) {
               throw new RuntimeException(e);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }


    }

    private void setName() throws IOException {
       System.out.print("Enter your name: ");
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        this.name = stdin.readLine();
        System.out.println();
    }
}
