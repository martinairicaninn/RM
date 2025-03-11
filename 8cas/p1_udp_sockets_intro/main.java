package p1_udp_sockets_intro;

/*
     TCP - uspostavljanje veze
     UDP - nema uspostavljanja veze
         - provera da li je sve poslato

     TCP - Transmission Control Protocol
     UDP - User Datagram Protocol
         - webex, stream, kad god imamo situaciju gde nije bitno da li je sve stiglo drugoj strani


 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class main {


    public static void main(String[] args) {

        try(DatagramSocket ds = new DatagramSocket()){

            //ako kod datagrama navedemo ADRESU i PORT to znaci da cmeo da ga saljemoi, a ako ne navedemo to znaci da ce se taqj datagram koristiti za prijem
            //prosledice se metodi recive
            //ako hocemo da ga saljemo imacemo buffer za salnje, velicinu koliko zelimo da posaljemo i imacemo host i port koji predtsija destinaciju
            //send je void metod
           // DatagramPacket toSend = new DatagramPacket(new byte[512], 512, host, port);
            //ds.send(toSend);

            //istu instancu koristimo da primimo paket
            //recive je void metod
            DatagramPacket toRecive = new DatagramPacket(new byte[512], 512);
            ds.receive(toRecive);

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }




}
