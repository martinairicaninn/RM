package p2_port_scanner;//OPIS PROGRAMA:
// RADI PORT SCAN
//IDEJA: IMAMO HOSTA I PROBAMO DA SE POVEZEMO NA SVE NJEGOVE PORTOVE I VIDIMO NA KOJIM PORTOVIMA IMA NEKI SERVER


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class PortScaner {


    public static void main(String[] args){

        //String host = "localhost";
        String host = "www.matf.bg.ac.rs";

        System.out.println("Start time: " + new Date());

        //za svaki soket kreiramo port
        //65546 - rqange za short
        for(int port = 1; port < 65536; port++) {
            /*
            \r znači "vrati kursor na početak linije", ali ne prelazi u novi red.
                To znači da svaki novi broj zamenjuje prethodni, pa se u konzoli vidi samo jedna linija
                 koja se stalno menja.
             */

            System.out.printf("\rTesting port:  %5d", port);


            //probamo da napravimo soket
            /*
            Ako Socket uspe da se poveže, port je otvoren, i ispisuje se poruka da je pronađen.
            Ako ne uspe (IOException), port je zatvoren, i program nastavlja dalje.
             */
            try (Socket s = new Socket(host, port)) {
                System.out.println("\rSocket data: " + s);
                System.out.println("Found @ " + new Date());


            }catch (UnknownHostException e){
                e.printStackTrace();
                break;


                //ako se desi IOExcweption mi nemamo server koji slusa na  portu za soket koji smo napravili
            } catch (IOException e) {
               //ignpred, idemo na naredni port

            }

        }

        System.out.println("\rEnd time: " + new Date( ));

    }

}