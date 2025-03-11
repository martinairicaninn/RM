package p5_pooled_weblog;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


//final označava da klasa ne može biti nasledđena. To znači da se neće moći praviti podklase
// od PooledWeblog klase.
final class PooledWeblog {


    //Atributi klase

    //Ovaj objekat omogućava čitanje linija iz ulaznog fajla (log fajl).
    private  final BufferedReader in;
    private final BufferedWriter out;
    private final int numOfThreads;

    //boolean finished: Ovaj atribut prati da li je obrada log fajla završena.
    private  boolean finished;


    //Lista u kojoj se čuvaju linije log fajla koje treba obraditi. Lista je sinhronizovana,
    // što znači da je sigurna za korišćenje u multithreaded okruženju.
    private final List<String> entries;   //RED POSLOVA

    //konstruktor
    PooledWeblog(FileInputStream in, PrintStream out, int threads) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new BufferedWriter(new OutputStreamWriter(out));
        this.numOfThreads = threads;
        // TODO

        //osiguranje da vise niti moze bezbedno da koristi istu listu
        this.entries = Collections.synchronizedList(new LinkedList<>());

    }

    //WORKER NITI SU LOOKUPRUNNABLE NITI , NJIH CEMO DA ORGANIZUJEMO TAKO DA UZIMAJU POSLOVE IZ LISTE POSLOVA
    // List<String> entires

    public void processLogFile() {

        //POKRENEMO WORKERE (niti)
        for(int i = 0; i < this.numOfThreads; i++){
            //svaka nit izvrsava LookupRunnable koji je odgovoran za obradu log linija
            //U ovom kodu, this se odnosi na trenutni objekat klase PooledWeblog.
            //Kada se koristi this unutar klase, ono označava "trenutni objekat" klase, tj. objekat
            // koji je pozvao metodu u kojoj se this koristi. U ovom slučaju, this se koristi za
            // prosleđivanje trenutnog objekta klase PooledWeblog kao argument konstruktoru objekta
            // klase LookupRunnable.
            Thread t = new Thread(new LookupRunnable(this));
            t.start();
        }

        try{

            //citamo linije fajla
            for(String entry = in.readLine(); entry != null; entry = in.readLine()){
                while (this.entries.size() > this.numOfThreads) {
                   /*
                   Proverava da li je lista entries (koja sadrži zadatke) veća od broja niti (numOfThreads).
Ako jeste, to znači da ima previše zadataka u redu i da bi dodavanje još moglo da preoptereti memoriju.
Zato se poziva Thread.sleep(1000 / this.numOfThreads);, što pauzira ovu nit na određeno vreme pre nego što ponovo pokuša da doda nove zadatke.
📌 Zašto 1000 / this.numOfThreads?

Ova vrednost predstavlja koliko dugo treba čekati pre nego što ponovo pokušamo da dodamo novi zadatak.
Primer: Ako imamo 5 niti, onda Thread.sleep(1000 / 5) = 200 ms (pauza od 200 ms).
                    */
                    Thread.sleep(1000 / this.numOfThreads);
                }

                //synchronized (this.entries) koristi se kako bi se osiguralo da samo jedna nit
                // može da dodaje elemente u listu u isto vreme (sinhronizacija).
                synchronized (this.entries){
                    //svaku liniju ubacamo u spisak poslova od pocetka
                    this.entries.add(0, entry);

                    //notifyAll() signalizira svim čekajućim nitima da mogu nastaviti rad.
                    this.entries.notifyAll();
                }



            }

            this.finished = true;    //da bismo znalida je posao gotov
            System.err.println("Work finished!");
            //notifyAll() se koristi kako bi se obavestile sve niti koje čekaju na posao da mogu da završe.
            synchronized (this.entries){
                this.entries.notifyAll();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //log je taj koji ima input i output sttreamovre on je taj koji moze da radi ispis
    //Ova metoda omogućava upisivanje linije u izlazni tok.
    //Ova metoda upisuje string entry u fajl (ili drugi izlazni tok)


    }

    void log(String entry) throws IOException {
        this.out.write(entry);   // Zapisuje liniju u izlazni tok
        this.out.newLine();      // Dodaje novi red (ENTER)
        this.out.flush();        // Osigurava da podaci budu odmah upisani
    }
    //Vraća listu svih linija koje čekaju na obradu ili su već obrađene.
    List<String> getEntries() { return this.entries; }

    //Vraća true ako je obrada završena, a false ako nije.
    boolean isFinished() { return  this.finished; }
}
