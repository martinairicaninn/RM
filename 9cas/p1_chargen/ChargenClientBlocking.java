package p1_chargen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class ChargenClientBlocking {

    public static void main(String[] args) {
        // Stream API -> byte[]
        // kada radimeo sa neblokirajucim koristimo kanale
        // kanali rade i read i write tako sto ne primaju byte bafere obicne jer bi smo mi onda morali da pazimo o svim indikatorima koje imamo
        //     capacitet, limit i position
        //   Channels API -> VyteBuffer, _Buffer koji automatski mere (c, l, p) pomeraju ove pokazivace interno (pripremi ga za citanje, pisanje)
        //    sve radi bafer za nas
        /*
            u opstem slucaju postoji FileChanel koji otvaraju kanal do fajla
            za komunikaciju preko mreze postoje SocketChanali
         */


        String host = "localhost";
        int port = 12345;

        SocketAddress address = new InetSocketAddress(host, port);

        try {
            SocketChannel client = SocketChannel.open(address);
            System.err.println("Connection established");

            //koristicemo  read i write samo sa jednim byte baferom
            //client.read()


            //za pisanje na std izlaz, pravimo Cannel od System.out (in)
            WritableByteChannel out = Channels.newChannel(System.out);
            //alociramo byte bufer odgovarajuceg kapaciteta
            ByteBuffer buf = ByteBuffer.allocate(74);

            //kada pkrenemo u debagmodu i pauziramo videcemo da stoji na readu jer je blokirajuce i zato radimo neblokirajuce 

            while(client.read(buf) != -1){
                //pripremamo bafer za pisanje
                //clear - postavlja poziciju na 0, a limit postavlja na kapacitet
                //        sto znaci da clear radimo pre citanja jer zelis vrv da popunis ceo bafer
                // flip - limit se postavlja an trenutnu poziciju,a pozicija se postavlja na 0
                //        ovo radimo nakon citanja
                //         kad smo procitali donekle postavimo limit na to mesto
                //         resetujemo postion, radimo write  i idemo redom do limita
                buf.flip();

                //write this buffer to stdout..
                out.write(buf);

                //pripremamo bafer za citanje
                //vracamo postion na 0, i citamo do limita sto znaci da moramo i limit da pomerimo na kapacitet sto radi clear da bi popunili ceo bafer
                buf.clear();

                System.err.println("Preformed one iteration");


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
