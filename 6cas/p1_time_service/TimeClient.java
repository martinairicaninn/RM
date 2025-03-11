package p1_time_service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

/*
Ovaj kod je Java implementacija klijenta za komunikaciju sa serverom koji koristi TIME protokol (port 37). Cilj je
dobiti trenutni UNIX timestamp od servera i prikazati ga kao datum i vreme. Evo detaljnog objašnjenja:

UNIX timestamp je standard za predstavljanje vremena kao broja sekundi koje su prošle od 1. januara 1970. u 00:00:00 UTC (poznato kao UNIX epoha).

Koristi se u računarstvu i mrežnim protokolima za jednostavno beleženje vremena bez obzira na vremensku zonu ili lokalne formate
 */

public class TimeClient {

    public static void main(String[] args) {
        String hostname = "time.nist.gov";
        int port = 373737;


        //razlika u peohama imdeju 1900 i 1970 u sekundama (ovo je 70 god u sekundama)
        long differenceInEpochsSeconds = 2208988800L;

        try(Socket client = new Socket(hostname, port)) {

            //Dobijamo ulazni tok (InputStream), koji omogućava čitanje podataka sa servera.
            InputStream in = new BufferedInputStream(client.getInputStream());



            //cetiri bajta
            // [0][0][0][0]
            // [ ][ ][ ][1] read nam vrati prvo batj broja or-ujemosa ovim iznad i siftujemo ga ulevo

            //[0][0][0][1]    <---   [0][0][1][0]    <---  [0][0][1][2]    <---- [0][1][2][0]  .....
            //[0][0][0][0]           [0][0][0][2]          [0][0][0][0]          [0][0][0][0]



            long secondsSince1900 = 0;

            //citmo 4 puta zog 4 bajta
            for(int i = 0; i < 4; i++){
                int b = in.read();

                //PRVO GA POMERIMO PA ORUJEMO  ZNACI AKO SMO NESTO PROCITALI SIFTUJEMO
                //shift za 1 <--- (pomeramo za 8jer pomeramo jedan bajt a akad pomeramo za 1 pomeramoiz ajedna bit)
                secondsSince1900 <<= 8;

                // OR
                secondsSince1900 |= b;

                // System.out.printf("%x  ", b);
            }


            //ova razlika su nam tih 70 god koje su visak
            long secondsScience1970 = secondsSince1900 - differenceInEpochsSeconds;
            long milisScience1970 = secondsScience1970 * 1000; //da dobijemo milisekunde
            Date now = new Date(milisScience1970);

            System.out.println("Its now " + now + " @ " + hostname);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

