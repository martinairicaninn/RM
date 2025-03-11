package p1_uvod_client_server;

//Operacije nad soketima
//connect - radio klijent (povezi se na neki remote host)
//send   - da saljemo podatke (da pisemo u soket)
// recv - da citamo iz soktea (da primamo podatke sa mreze)
//close - da zatvorimo taj soket (da zatvorimo mrezni kanal)
//------ (ONO STO RADE SERVERI NA DALJE)
//bind - za vezivanje soketa za odgovarajuci port      --->    TCP / UDP  (transportni sloj)
//listen* (blokirajuci poziv dok se ne uspostavi veza)
//accept - radimo nad soketom (prihvatimo klijenta, dobijemo soket do njega i preko tog soketa do klijenta kopmuniciramo)
//       |_  komunikacija jse vrsi u oba smera



//OPIS PROGRAMA:
// RADI PORT SCAN
//IDEJA: IMAMO HOSTA I PROBAMO DA SE POVEZEMO NA SVE NJEGOVE PORTOVE I VIDIMO NA KOJIM PORTOVIMA IMA NEKI SERVER


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketInfo {


    public static void main(String[] args){
        //otvaramo g au try-catch metodu jer ako neko neocekivano prekine komunikaciju obezbedili samo da se uhvati greska

        try {
            //Socket s = new Socket("hostname", 12345);
            Socket s = new Socket("www.matf.bg.ac.rs", 80);


            //komuniciramo tako sto nam getInputStream vraca InputStream sa druge strane i obavezno gha baferisemo
            InputStream in = new BufferedInputStream( s.getInputStream());
            OutputStream out = new BufferedOutputStream(s.getOutputStream());

            System.out.println(s);

            //s.getPort() - daj port na koji smo naveli
            //s.getLocalPort - daj lokalni port za raj soket
            //s.getInetAdress - daj mi adrsu porta na koji smo se vezali


          /*  in.read();
            out.write();

            //kad zavrsimo sa soketom - zatvorimo ga
            s.close();

           */


            //ZA SERVER SE KORISTI KLASA ServerSoket - moze bez argumenata ili sa jednim koji predstavlja port

            int port = 9000;
            try (ServerSocket ss = new ServerSocket()) {
                //serveri u gl u beskonacnoj petlji
                while(true) {

                    //vezi se a port, ako ne uspe vezmo ga za drugi
                    // ss.bind(new InetSocketAddress(port));

                    //listen i accepte radimo pod jednom metodom
                    //accept - ceka na acceptu(blokira) sve dok se ne ostvari veza sa nekim klijentom i vraca soket koji predstavlja tu vezu izmedju klijenta
                    //dobijamo soket koji predstavlja klijenta i onda sa klijentom komuiniciramo
                    Socket client = ss.accept();
                               // client.getInputStream().read();         //dobili stream do tog klijenta i onda radimo sta god zelimo

                    //metod koji opsluzuje klijenta
                    serve(client);
                }


            } catch ( IOException e){
                e.printStackTrace();
            }






        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private static void serve(Socket client) {
    }

}
