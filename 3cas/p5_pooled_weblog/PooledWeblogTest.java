package p5_pooled_weblog;

//MultiThreaded
// NE ZELIMO DA IMAMO KREIRARENJE NOVE NITI ZA SVAKU LINIJU
//ZELIMO DA IMAMO UNAPRED SPREMNE NITI
//IMACEMO 1 THREAD KOJI RADI PARSIRANJE FAJLA
//I SABMITOVANJE TASKOVA NA POOL
//OSTALI TREDOVI KOJI SU WORKER TREDOI CE ZAPRAVO SAMO A UZIMAJU TASKOVE I DA IH OBRADJUJU

import java.io.FileInputStream;
import java.io.FileNotFoundException;

final class PooledWeblogTest {

    public static void main(String[] args) throws FileNotFoundException {

        //KADA NEKO ZELI DA NAPRAVI NEKU APL KORISTI KLASU KOJU MI ISPORUCUJEMO
        //TA KLASA JE PooledWeblog KOJA CE DA RADI PROCESIRANJE FAJLA I LOOKUP
        PooledWeblog pw = new PooledWeblog(
            new FileInputStream("p5_pooled_weblog/apache.logfile"),    //DAJEMO INPUTSTREAM DO LOG FAJLA
            System.out,            //DAJEMO PUTPUT STREAM GDE CE DA SE SIPISUJE REZULTAT
            6                      //broj niti koje ce se koristiti za obradu
        );

        //Pokreće se obrada log fajla: Kada pozovemo ovu metodu, započinje čitanje log fajla
        // liniju po liniju.
        pw.processLogFile();

    }
}
