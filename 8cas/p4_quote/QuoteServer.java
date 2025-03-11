package p4_quote;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Date;

/*
    ovde je server organizovan kao thread
    ideja je kada se kreira serverska klasa otvori se soket kooji ce server da koristi
    buffer reader - je citac fajla
 */

final class QuoteServer extends Thread {
    public static final int PORT = 12345;

    private final DatagramSocket socket;
    private BufferedReader in;


    public static void main(String[] args) throws IOException {
        new QuoteServer().start();
    }


    private QuoteServer() throws IOException {
        this.socket = new DatagramSocket(PORT);
        //ako ne uspemo da ucitamo fajl zbog toga sto on ne postoji ili nesto je problem onda cemo sluziti trenutno vreme umesto citata
        try {
            this.in = Files.newBufferedReader(Paths.get("p4_quote/one_liners.txt"));
        } catch (NoSuchFileException e){
            System.err.println("Couldn't open quote file. Serving time instead.");
        }
    }


    /*
        ista prica imamo beskonacnu ptelju,
                            cekamo request
                            dobijamo request
     */

    @Override
    public void run() {
        System.err.println("Quote server listening on port " + PORT);
        try {
            while (true) {
                //cekamo request
                byte[] buf = new byte[512];
                DatagramPacket request = new DatagramPacket(buf, buf.length);
                //dobijamo request
                this.socket.receive(request);

                System.err.println("Received packet.");

                //daje podatke koje treba da saljemo klijentu
                buf = getDataToSend();
                //ako nemamopodatke prekidamo
                if (buf == null)
                    break;

                //ako je sve ok mi taj bajt niz saljemo dalje
                System.err.println("Sending data to client..");
                /*
                    kako ga saljemo?
                    ista prica, pravimo paket sa tim baferom, te velicine, adresa i port su iz requesta pokupljeni
                 */
                DatagramPacket response = new DatagramPacket(buf, buf.length,
                        request.getAddress(),
                        request.getPort());
                this.socket.send(response);
                System.err.println("Client served.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            //zatvaranje soketa kada zavrsimo
        } finally {
            this.socket.close();
        }
    }

    private byte[] getDataToSend() throws IOException {
        //proverava da li smo incijalizovali nas rider
        //ako jesmo super uzmi sledecu liniju
        //a ako nismo onda sluzis trenutno vreme
        //posto uzimamo in.readLine() moze da vrati null ako smo stigli do kraja fajla
        //u protivnome pristupamo bajtovima stringa - data.getBytes() da bismo dobili bajt niz koji saljemo
        String data = this.in == null ? new Date().toString() : this.in.readLine();
        return data == null ? null : data.getBytes();
    }
}