package p1_chargen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/*          selector

            S(server)  C1(client)    C3
            A(accept)   W(write)   W(write)
                         buffer     buffer



 */

public class ChargenServer {

    public static void main(String[] args) {


        byte[] rotation = new byte[95 * 2]; //duplirali niz
        for (byte i = ' '; i <= '~'; i++) {  // i = ' '  --> krenuli smo od space-a, sledece ide !, a ! - SPACE = 1 ( 21 - !, 20 - SPACE), na mesto 1 pisemo SPACE
            // " --> 22, na mesto 2 pisemo " i itd
            //posto smo duplirali niz (kao d aimamo dva niza) pisemo karakter u oba niza, u prvom pisemo:  rotation[i - ' '] = i;
            int index = i - ' ';
            rotation[index] = i;
            rotation[index + 95] = i;

        }


       try (ServerSocketChannel server = ServerSocketChannel.open();
                //imprementiramo selector
        Selector selector = Selector.open()) {

           //provera selector
           //proveravamo da li smo uspeli lepo da otvorimo kanal, ako nismo da ispisemio neku poruku
           if(!server.isOpen() || !selector.isOpen()){
               System.err.println("nisi uspeo da otvoirs kanal");
               //u tom slucju izlazimo
               System.exit(1);
           }

           //u protivnom bind-ujemo ovaj kanal koji imamo i mozemo koristiti lokalnuu soket adresu
            server.bind(new InetSocketAddress(12345));
           //postavljamo da koje god operacije radili na serveru nece nas blokirati
           server.configureBlocking(false);

           //dodajemo nas serverski kanal da ga posmatra selektor
           //metod register -->  registurj da ovaj kanal posmatra ovaj selector za ovu operaciju
           //               -->  OPERACIJE: to je zapravo int, ali te konstante imamo u SelectionKey,
           //               --> serverski kanal radi samo ACCEPT zato ide OP_ACCEPT
           server.register(selector, SelectionKey.OP_ACCEPT);


           //SERVERSKA PETLJA
           //                  treba u jednoj iteraciji da proverimio ko je sve spreman, da dobijemo  spisak spremnih i da krecemo se po tom spisku i radimo operacije
           //                  1. hvatamo samo one koji su spremni ---> raidimo select sistemski poziv
           //                        select ---> prolazi kroz sve kanale, testira da li je kanal spreman za neku operaciju (ona koja je bila dodeljena)
           //                               ---> vraca broj selektovanih kanala
           //                 selectedKeyes ---> vraca skup selektovanih kljuceva

           while (true){
               selector.select();

               //dohvati selektovane kanale (svi selektovani kanali)
               Set<SelectionKey> selectionKeySet = selector.selectedKeys();
               Iterator<SelectionKey> it = selectionKeySet.iterator();      // iteriramo kroz skup
               while (it.hasNext()) {
                   SelectionKey key = it.next();    //dohvatili smo ga
                   //kada uhvatimo kljuc, takodje ga uklonimo
                   it.remove();

                   //sad treba da testitramo taj kljuc, da saznamo koja je operacija u pitanju i da odradimo u zavisnosi sta treba
                   //kada obradimo neki key moramo da ga uklonimo


                   try {
                       //testiramo nas key
                       if (key.isAcceptable()) {  //ako je acceptable onda on pripada serverskom kanalu
                           ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                           SocketChannel client = serverChannel.accept();  //daje nam kanal do klijenta

                           //takodje konfigurisemo klijenta da ne blokira(jer podrazumevano blokiraju)
                           client.configureBlocking(false);


                           //debag
                           System.err.println("Accepted connection from " + client.getRemoteAddress());

                       /*
                            dobili smo kanal
                            treba da ga ubaimo u selector (posmatraj sad i ovaj kanal)
                            bice operacija write
                            formiracemo buffer za njega
                        */

                           //registrujemo naseg klijenta da ga as selector posmatra
                           SelectionKey clientKey = client.register(selector, SelectionKey.OP_WRITE);    // jer sa klijentima radimo write i registar ce vratiti selection key


                           ByteBuffer buf = ByteBuffer.allocate(74);
                           //ubacamo u buffer (sta, odakle, koliko)
                           buf.put(rotation, 0, 72);
                           buf.put((byte) '\r');
                           buf.put((byte) '\n');

                           buf.flip();

                           //dodajemo mu bafer koji smo napravili
                           clientKey.attach(buf);
                       } else if (key.isWritable()) {   //znai da je nas kanal zapravo klijent  --> znaci treba dohvatiti bafer i uraditi write
                           // (po potrebi zamniti novom linijom ako smo ispisali ceo bufer)

                           //dohvatimo kanal koji je u pitanju
                           SocketChannel client = (SocketChannel) key.channel();
                           //gledamo koji je bufer u pitanju
                           ByteBuffer buf = (ByteBuffer) key.attachment();

                           //proveramo prvo da li smo ispisali ceo bafer
                           if (!buf.hasRemaining()) {   //da li je position stigao limit(ako jeste nemamo remaining i onda samo zamenimo nas bafer
                               buf.rewind();  //vraca position na 0 (vratili se na pocetak)

                               //uzimamo prvi karakter nadjemo njegovu poziciju u nizu rotation
                               byte first = buf.get();
                               int pos = first - ' ' + 1;
                               //opet se vracamo na pocetak
                               buf.rewind();
                               //ubacamo
                               buf.put(rotation, pos, 72);
                               buf.put((byte) '\r');
                               buf.put((byte) '\n');

                               buf.flip();


                           }

                           client.write(buf);

                       }


                   } catch (IOException e) {
                       key.cancel();   //u slucaju da se jedan samo klijent iskljuci posto mozemo pokrenuti vise klijenata
                       try {
                           key.channel().close();
                       } catch (IOException ex) {
                           //ignored
                       }
                   }
               }

           }




       } catch (IOException e) {
           e.printStackTrace();
       }


    }

}


/*
 UVEK KADA RADIMO SA KANALIMA PITAMO SE DA LI ZELIMO BLOKIRANJE ILI NE
 AKO RADIMO SA KANALIMA OBICNO ZELIMOI NEBLOKIRAJUCE, A PODRAZUMEVANO UVEK BLOKIRAMO (KAKO GOD SE ZVALA TA ZENA)
 */