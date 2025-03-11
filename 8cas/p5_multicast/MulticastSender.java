package p5_multicast;

import java.io.*;
import java.net.*;

    /*
        Sender salje 10 paketa
        Sniffer prima 10 paketa
     */

/*
    broadcast - ako jedan cvor svim drugim cvorovima salje
    unicast - ako samo jednom cvoru saljemo
    anycast - kada bilo kom u nekoj topologiji saljemo nesto sto nije naznaceno tacno kome, moze bilokome
    multicast - kada vise hostova  ucestvuje u jednoj grupi i onda kada nesto saljemo to ide citavoj grupi
              - kada neko posalje u grupin poruku, svi koji su subscribovani toj grupiu na tom portu primaju tu poruku

    Primeri programa koji koriste multicast:
         ruter, smarttv
 */

final class MulticastSender {

    public static void main(String[] args) throws UnknownHostException {

        //imas grupu istu kao kod sniffera
        InetAddress addr = InetAddress.getByName("all-systems.mcast.net");
        //isti je port takodje
        int port = 4000;

        //timnetolive (ttl) sluzi da nasi paketi ne bi beskonacno lutali mrezom
        /*
               ideja:
               u pocetku postavimo ttl npr za 10 paketa
               posaljemo paket
               kroz svaki ruter kroz koji prodje paket smanjuje se ttl za 1
               npr nas ruter je primio paket koji ma ttl 4, smanjujemo ttl na 3 i prosledjujemo ga dalje
               tako sprecavamo u slusaju da paket nema destinaciju da on beskonacno luta mrezom i ogranicavamo koliko ce nasi paketi ziveti
               kod multicasta ttl je jako bitan jer ako nasi racuari u okviru jedne ucionice se povezu na istu mulitcast grupu ako posatimo ttl nasih apketa koje saljemo toj
                   grupi ako posatavimo ttl na 1 nasi paketi nece izaci iz lokalne mreze sto znaci da neko spolja ko slusa na istoj multicast grupi nece uopste primiti nase
                   poruke, otp je 20 dovoljno da dostigne bilo koga na internetu
              sa ttl kontorolisemo koliko nasi paketi idu daleko
              u prevodu koliko treba hopova od mene do nekog cvora u prevodu koliko izmedju nas ima drugi cvorova
                 ako treba 5 hopova od mene do destinacije trbace nam ttl bar 5 da bi moji paketi stigli do destinacije (ttl na mreznom sloju)
         */
        byte ttl = (byte)1;

        //imamo multicastdate-a koju saljemo
        byte[] data = "Here's some multicast data\r\n".getBytes();
        //kreiramo datagramPacket
        DatagramPacket dp = new DatagramPacket(data, data.length, addr, port);
        //kreiramo multicast socket
        try (MulticastSocket ms = new MulticastSocket()) {
            ms.setTimeToLive(ttl);
            //udejmo u grupu
            ms.joinGroup(addr);
            //posto isti paket saljemo 10 puta kreiramo ga unapred
            for (int i = 1; i < 10; i++)
                //sendujemo 10 puta isti paket
                ms.send(dp);
            //izlazimo iz grupe
            ms.leaveGroup(addr);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}