package p2_version;

import java.net.InetAddress;
import java.net.UnknownHostException;

//Opis programa:
//Ovaj kod ima za cilj da prikaže informacije o IP adresama domena (IPv4 i IPv6)
// i da analizira njihovu verziju

public class Main {

    public static void main(String[] args) throws UnknownHostException {


        InetAddress addressv4 = InetAddress.getByName("www.matf.bg.ac.rs");

        // getHostAddress() - pozivanjem ove f-je dobijamo IP adresu kao string tj "147.91.8.5"
        System.out.println(addressv4.getHostAddress());

        //STAMPA BAJTOVE ADRESE (0-255)
        printAddress(addressv4.getAddress());

        //PROVERAVA DUZINU BAJTOVA I ODREDJUJE VERZIJU (IPv4 = 4)
        System.out.println("IPv" + getVersion(addressv4));
        System.out.println();

        InetAddress addressv6 = InetAddress.getByName("ipv6.google.com");
        System.out.println(addressv6.getHostAddress());
        printAddress(addressv6.getAddress());
        System.out.println("IPv" + getVersion(addressv6));

    }



    //reutrns version of address
    //PRONALAZI VERZIJU IP ADRESE KORISTECI BROJ BAJTOVA
    private static int getVersion(InetAddress addr) {

        //posto getAdress vraca adresu kao niz i onda prebrojimo koliko elemenata ima taj niz
        switch (addr.getAddress().length){
            case 4:
                return 4;
            case 16:
                return 6;
            default:
                return -1;
        }


       /* if(addr instanceof Inet4Address)
            return 4;
        else if (addr instanceof Inet6Address)
            return 6;
        else
            return -1; */

    }

    //print adresses in appropriate format
    //STAMPA IP ADRESU  U LJUDSKOM FORMATU
    private static void printAddress(byte[] addr) {

        if(addr.length != 4){
            throw new IllegalArgumentException("This metod works only for v4 adresses");

        }

        //Zašto b < 0 ? b + 256 : b?
        //Bajtovi u Javi su potpisani (-128 do 127). IP adrese su u opsegu 0-255.
        //Ovaj uslov konvertuje negativne vrednosti u pozitivne.
        for(byte b : addr){
            //before we print the byte we need to modify it as need
            int unsignedByte = b < 0 ? b + 256  : b;
            System.out.print(unsignedByte + " ");
        }

        System.out.println();

    }

    //Negatie numbers we get when we turn to 128
}
