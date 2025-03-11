package p01_intro;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class SSLClientSocketsIntro {


    /*
    Secure Sockets Layer (SSL) je standardna bezbednosna tehnologija za uspostavljanje šifrovane veze između servera i klijenta—obično između veb servera (sajta)
    i pregledača, ili između mejl servera i mejl klijenta (npr. Outlook).

    SSL omogućava sigurno prenošenje osetljivih podataka poput brojeva kreditnih kartica, brojeva socijalnog osiguranja i podataka
    za prijavljivanje.

    Uobičajeno, podaci koji se razmenjuju između pregledača i veb servera šalju se kao običan tekst, što može ostaviti korisnike ranjivim na prisluškivanje.
    Ako napadač uspe da presretne komunikaciju između pregledača i servera, može videti i zloupotrebiti te informacije.

    Tačnije, SSL je sigurnosni protokol. Protokoli definišu kako se koriste kriptografski algoritmi. U ovom slučaju, SSL protokol određuje način šifrovanja kako
    same veze, tako i podataka koji se prenose.

    Svi moderni pregledači mogu komunicirati sa sigurnim veb serverima pomoću SSL protokola, ali da bi uspostavili sigurnu vezu, pregledač i server moraju imati
    SSL sertifikat.
     */


    public static void main(String[] args) {

        //ovo je nesto sto bi se redilo na klijentskoj strani kada se uspostavlja veza sa nekim hostom

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try (Socket socket = factory.createSocket("host_address_goes_here", 1234)){
            Writer out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            out.write("somthing.....");
            out.flush();
            out.close();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
