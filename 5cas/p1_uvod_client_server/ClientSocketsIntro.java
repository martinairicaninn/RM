package p01_client_sockets_intro;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

final class ClientSocketsIntro {

	/*

            Soket je veza između dva računara (hosta). Može da izvrši sedam osnovnih operacija:

      c/s    1.  Poveže se na udaljeni računar
      c/s    2.  Pošalje podatke
      c/s    3.  Primi podatke
      c/s    4.  Zatvori vezu
        s  5.  Poveže se na port
        s  6.  Osluškuje dolazne podatke
        s  7.  Prihvata veze od udaljenih računara na povezani port

            Java klasa Socket, koja se koristi i za klijente i za servere, ima metode koje odgovaraju
            prvih četiri od ovih operacija. Poslednje tri operacije su potrebne samo serverima, koji
            čekaju da se klijenti povežu sa njima. One su implementirane u klasi ServerSocket.
            Java programi obično koriste klijentske sokete na sledeći način:

           * Program kreira novi soket koristeći konstruktor.
           * Soket pokušava da se poveže na udaljeni računar.
           * Kada se veza uspostavi, lokalni i udaljeni računar dobijaju ulazne i izlazne tokove sa soketa
            i koriste te tokove da šalju podatke jedan drugom. Ova veza je full-duplex; oba računara
            mogu simultano slati i primati podatke. Šta podaci znače zavisi od protokola; različite
            komande se šalju FTP serveru i HTTP serveru. Obično postoji neki dogovoreni proces
            usklađivanja (hand-shaking), nakon kojeg sledi prenos podataka od jednog do drugog računara.
            Kada se prenos podataka završi, jedna ili obe strane zatvore vezu. Neki protokoli, kao što je
            HTTP 1.0, zahtevaju da veza bude zatvorena nakon svake obrade zahteva. Drugi, kao što je
            FTP, omogućavaju da se više zahteva obradi u jednoj vezi.
	 */

    // Read more about sockets
    // @ https://docs.oracle.com/javase/tutorial/networking/sockets/definition.html

    public static void main(String[] args) {
        try (Socket sock = new Socket("hostname", 80)) {

            // Send/Receive data...
            OutputStream out = new BufferedOutputStream(sock.getOutputStream());
            InputStream in = new BufferedInputStream(sock.getInputStream());

        } catch (UnknownHostException ex) {
            System.err.println("The specified hostname is unknown.");
        } catch (IOException ex) {
            System.err.println("Connection failed.");
        }
    }

}