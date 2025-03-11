package p2_daytime_service;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;

public class DaytimeClient {

    //trebace nam konstante neke, tipa hots


    public static void main(String[] args) {

        String hostname = "localhost";

        try (DatagramSocket socket = new DatagramSocket()) {  //ne navodimio port, putamo da se iskoristi neki drugi

            //ako samo pokrenemo klienta a nismo pokrenuli server jer nemamo kome da posaljemo zahtev
            socket.setSoTimeout(5000);


            InetAddress host = InetAddress.getByName(hostname);

            DatagramPacket request = new DatagramPacket(new byte[1], 1, host, DaytimeServer.PORT); //host ne moze da se daje kao string nego mora kao InetAddres
            socket.send(request);

            //mi ovde tumacimo odgoor iz paketa
            DatagramPacket response = new DatagramPacket(new byte[DaytimeServer.BUF_SIZE], DaytimeServer.BUF_SIZE);
            socket.receive(response);

            //tumacimo string
            String date = new String(response.getData());
            System.out.println(date);

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
