package p4_network_interfaces;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Comparator;
import java.util.Enumeration;

//OPIS PROGRAMA:
//Ovaj Java kod prikazuje kako se mogu dobiti i prikazati mrežni interfejsi i njihove IP adrese.
// Kroz ovaj program možemo dobiti informacije o mrežnim interfejsima računara (kao što su Ethernet
// ili Wi-Fi interfejsi) i njihove povezane IP adrese. Kod koristi i Enumeration za iteraciju kroz
// mrežne interfejse, kao i Stream API za efikasniji rad sa podacima.

public class Main {

    public static void main(String[] args) throws SocketException {

       // InetAddress local = InetAddress.getLoopbackAddress();
       // System.out.println(NetworkInterface.getByInetAddress(local));

        //NetworkInterface.getNetworkInterfaces() vraća sve mrežne interfejse sa računara.
        // Ovo je Enumeration, što znači da omogućava da iteriramo kroz sve mrežne interfejse na
        // računaru.
        Enumeration<NetworkInterface> interfases = NetworkInterface.getNetworkInterfaces();
        //dok ima elemenata mozemo da setamo po interfejsu
        while(interfases.hasMoreElements()){
            //dohvatimo konkretni mrezni intefejs
            NetworkInterface ni = interfases.nextElement();
            //ispisujemo ga
            System.out.println(ni);

            //System.out.println(ni) ispisuje informacije o mrežnom interfejsu,
            // kao što su njegov naziv, prikazivano ime i indeks.
            System.out.println("\t" + ni.getName() + " " + ni.getDisplayName() + " " + ni.getIndex());

            //prolzimo kroz sve adrese ovog mreznog intyerfejsa i ispisujemo ih
            //Dobijanje i ispisivanje IP adresa mrežnog interfejsa:

            Enumeration<InetAddress> address  =  ni.getInetAddresses();
            while(address.hasMoreElements())
                System.out.println("\t\t" + address.nextElement());
        }

        System.out.println("============================================================================");

        //sve ovo pomocu streama (DOUCITI AKO BUDE BILO POTREBNO)
        NetworkInterface.networkInterfaces()
                //f-ja koja konvertuje interface u int
                .sorted(Comparator.comparingInt(NetworkInterface::getIndex))
                //mapiramo interface u string
                .map(ni -> ni.toString() + "\n\t" + ni.getName() + " " + ni.getDisplayName() + " " + ni.getIndex()
                + "\n" + ni.inetAddresses()  //cetamo kroz sve ip adrese
                        .map(InetAddress::toString) //za svaku od ip adresa mapiraj je u string
                        . reduce((acc, ip) -> acc+ "\n\t" + ip)  //spojiumo ih  sa novim redom i da budu uvucene (acc- akumulirani string)
                        .orElse("no addressses")
                )
                .forEach(System.out::println);

    }
}
