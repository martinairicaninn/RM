package p3_host_lookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

//Opis programa:
//Program omogućava korisniku da unese hostname (npr. www.google.com) ili IP adresu (npr. 172.217.20.4)
// i vrati odgovarajuće informacije:
//
//Ako korisnik unese hostname, program vraća odgovarajuću IP adresu.
//Ako korisnik unese IP adresu, program vraća odgovarajući hostname.
//Ako uneti podatak nije validan hostname ili IP adresa, program prikazuje poruku o grešci.
public class Main {

    public static void main(String[] args){

        //Ulaz podataka: Koristi BufferedReader za unos podataka iz konzole.

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter names/adresses or \"exit\" to quit");
            while(true){
                //ucitaj sa readline-a sledecu liniju
                //Metoda readLine() čita celu liniju teksta koju korisnik unese i vrati je kao String.
                //Čitanje traje sve dok korisnik ne pritisne Enter.
                String host = in.readLine();
                if(host.equalsIgnoreCase("exit") || host.equalsIgnoreCase("quit"))
                    break;
                //lppkup will do appropriate logic on host
                //Poziv metode lookup: Za svaki unos korisnika, metoda lookup se koristi
                // za određivanje da li je uneto ime hosta ili IP adresa, i vraća odgovarajuće informacije.
                System.out.println(lookup(host));

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //returns string who is the result of work
    private static String lookup(String host) {

        InetAddress addr;
        //we look for is this host actually host name or ip adress
        //if is hostname we returns ip adress of host, if  ip adress we returns his hostname
        try {
            addr = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            return "Cannot find host " + host;
        }
        if(isHostname(host))
            return addr.getHostAddress();
        else
            return  addr.getHostName();

    }


    //is string equals hostname or ip adress
    private static boolean isHostname(String host) {
        //hostname
        //ipv4
        //ipv6 (ako sadrzi :)

        //onda je ovo v6 nije hostname pa vraca false
        if(host.indexOf(':') != -1)
            return false;

        //hostname
        //ipv4

        // \\ da bi smo razdvojili stringove tamo gde vidimo tacke jer . u regexu oznacava bilo koji karakter
        //    zato se mora \\

        /*
        host = "www.google.com"
        → split("\\.") rezultira nizom:
        ["www", "google", "com"]

        host = "192.168.1.1"
        → split("\\.") rezultira nizom:
        ["192", "168", "1", "1"]


         */


        String[] parts = host.split("\\.");
        //if lenght  different od 4 than is hostname
        //AKO BROJ DELOVA NIJE 4 SMATRA SE DA JE HOSTNAME
        if(parts.length != 4)
            return true;

        //if characters in parts different of digits than is hostanme

        for(String part : parts){
            for(char c : part.toCharArray()){
                //AKO KARAKTERI NISU CIFRE SMATRA SE DA JE HOSTNAME
                if(!Character.isDigit(c))
                    return true;

            }
        }


        //za svaki part u partovima proveravamo da li bilo koji part u njegovim chroovima ima nek ane cifra
        //i ako je to tacno ceo izraz je tacan i to znaci da je hostname a ako nije tacno (sve su cifre) to
        //to je onda ip adresa
       /*
        return Arrays.stream(parts)
                .anyMatch(part -> part.chars().anyMatch(c -> !Character.isDigit(c)));
                */


        return false;


    }
}

