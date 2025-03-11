package p5_multicast;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;


    // MulticastSniffer u treminalu ispisuje sta nekjo salje ovoj multicast adresdi
    //mozemo pkrenuti gomilu MulticastSendera i onda mMulticastSnifferi primaju stavri koje je neko drugi posalo u istoj grupi

final class MulticastSniffer {

    public static void main(String[] args) throws UnknownHostException {

        //treba nam ta grupa  i ovo je adresa koja se rizolvuje u neku multicast adresu cisto da bude citljivije
        InetAddress group = InetAddress.getByName("all-systems.mcast.net");
        //port koji koristimo je neki random
        int port = 4000;


        MulticastSocket ms = null;
        try {
            //kreiramo multicast socket tako sto koristimo ovaj port
            ms = new MulticastSocket(port);
            //moramo nekako da kazemo da hocemo da se prikljucimo toj grupi i to radimo sa join koji prikljucuje multicastSocket toj grupi koju smo prosledili
            //joinGroup - prima multicastadresu
            ms.joinGroup(group);

            /*
                ovaj program samo snifuje sta se desva u grupi
                ukljucimo se u grupuy i samo listamo sta se salje u grupi nista rugo ne radimo
             */

            //imamo bafer za primanje
            byte[] buffer = new byte[8192];

            //noinspection InfiniteLoopStatement
            //kako stizu Datagrami ti to primaj
            //primamo tako sto napravimo Datagrram packet sa ovim baferom i uradimo receive
            while (true) {
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                ms.receive(dp);
                //kada primimo to ce vrv biti neki string, pa ga konvertujemo u string i prikazemo
                String s = new String(dp.getData(),0, dp.getLength(), StandardCharsets.ISO_8859_1);
                System.out.print(s);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            //zatvara se multicastSocket
        } finally {
            if (ms != null) {
                try {
                    //tako sto moze da se izadje iz grupei to pre zatvaranje grupe
                    ms.leaveGroup(group);
                    //moze da se zatvori
                    ms.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
    }
}