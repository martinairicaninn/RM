package p2_daytime_service;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaytimeServer {

    public static final int PORT = 12345;
    public static final int BUF_SIZE = 1024; //max velicina buffer

    //server ce biti aplikacija koja se ce koristiti datagram socket instancu da primi pakete od klijenta i onda nazad vrati trenutno vreme
    //server mora da ima indikator da postoji klijent
    //ovde nema uspostavljanje veze i server u koliko zeli da posalje vreme klijentu trenutno ne moze da uradi to tek tako, mora da ima indikator da
    //  je klijent zapravo tu da bi znoa kome da posalje
    //kod TCP soketa kada uspostavimo vezu imamo soket instancu, znamo koja je njegova adresa, koji je njuegov port, a ovde ne znamo nista od toga
    //ovde te informacije dovlacimo tako sto mora klijent nesto prvo da posalje i onda iz Datagrama koji je klijent poslao da izvucmeo informacije koje su nam
    //   potrebne: ko si ti?   koja ti je adresa?   koji ti je PORT?
    //           i vraticmeo istoj toj adresi istom tom portu informacije koje treba da vratim


    private static final Logger audit = Logger.getLogger("requests");
    private static final Logger errors = Logger.getLogger("errors");

    public static void main(String[] args) {

        // prvo nam treba jedan Datagram Socket
        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            //serevr i prima i salje preko istog soketa
            //za razliku od TCP-a nemamo serverski soket i soket za svakog klijenta jer nemamo klijenta. Nama stizu Datagrami jedan po jedan
            //kad stigne Datagram pogledamo ko je poslao i vratimo odg istom tom klijentu


            //cekamo paket od klijenata koji nazivamo request
            while (true) {


                    //                                         koliko je moguce popuniti, koliko zelimo da pomunimo
                    DatagramPacket reqest = new DatagramPacket(new byte[BUF_SIZE], BUF_SIZE);
                    //recive nas blokira ovde sve dok ne dobijemo neki Datagram, kada ga prodjemo znaci da je stigao neki Datagram
                    socket.receive(reqest);

                    audit.info("Request recived from client");


                    //treba izvuci sad informacije i pravimo Datagram za slanje, koji popunjavao potrebnim informacijama koje saljemo
                    //treba nam trenutno vreme

                    String daytime = new Date().toString();
                    //koji format zelimo
                    byte[] date = daytime.getBytes(StandardCharsets.UTF_8);

                try {
                    //sta saljemo, koja duzina je u pitanju, uzimamo adresu posiljaoca i port sa kog je poslao i vracamo na njih
                    DatagramPacket response = new DatagramPacket(date, date.length, reqest.getAddress(), reqest.getPort());
                    socket.send(response);


                    audit.info("Sent response to client");

                } catch (IOException e) {
                    errors.log(Level.SEVERE, e.getMessage(), e);
                }




            }

        } catch (IOException e) {
            errors.log(Level.SEVERE, e.getMessage(), e);
        }


    }
}
