package p1_intro;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

//prevođenje imena domena u IP adrese, rad sa lokalnim mrežnim adresama i upoređivanje IP adresa.

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        //IP (Internet Protocol)
        // -v4: 4B (32b)   |  [0-255].[0-255].[0-255].[0-255]  192.168.*.*
        // -v6: 16B (128b) |  [0-9a-f]{4} :: ....    23bf:2345:0:2f3d:...:2234
        // hostname (math.rs)  ---- DNS ---->  123.123.123.123

        //predstavlja bilo koju ip adresu
        InetAddress addr;
        //konkretno IPv4 adresa
        Inet4Address v4addr;
        //IPv6 adresa
        Inet6Address v6addr;

        //DOBIJANJE IP ADRESE ZA ZADATO IME DOMENA
        System.out.println(InetAddress.getByName("www.google.com"));
        System.out.println(InetAddress.getByName("www.facebook.com"));
        System.out.println(InetAddress.getByName("172.217.20.4"));
     //  System.out.println(InetAddress.getByName("www.v6.facebook.com"));

        //give me all addresses which correspond by host
      //getAllByName returns Array, because we use Arrays.toString which prints string
     // System.out.println(Arrays.toString(InetAddress.getAllByName("www.v6.facebook.com")));
        //VRACA NIZ SVIH IP ADRESA KOJE ODGOVARAJU DATOM DOMENU
        System.out.println(Arrays.toString(InetAddress.getAllByName("www.google.com")));


        //VRACA IP ADRESU VASEG RACUNARA NA LOKALNOJ MREZI
        System.out.println(InetAddress.getLocalHost());

        //VRACA 127.0.0.1 STO JE POVRATNA ADRESA RACUNARA
        System.out.println(InetAddress.getLoopbackAddress());

        //PRETVARANJE  IMENA DOMENA U IP ADRESE
        System.out.println(InetAddress.getByName("www.math.rs"));

        // getAddresss VRCA IP ADRESU KAO NIZ BAJTOVA
        //NPR IP ADRESA 192.168.0.1 POSTAJE [192, 168, 0, 1]
        System.out.println(Arrays.toString(InetAddress.getByName("www.math.rs").getAddress()));


        //UPOREDJIVANJE IP ADRESA RAZLICITIH DOMENA
        InetAddress matf1 = InetAddress.getByName("www.math.rs");
        InetAddress matf2 = InetAddress.getByName("www.matf.bg.ac.rs");
        // returns true becauce their adresses is equal although their host names are  different
        System.out.println(matf1.equals(matf2));




    }
}
