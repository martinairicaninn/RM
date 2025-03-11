package p5_pooled_weblog;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

//Klasa LookupRunnable implementira Runnable interfejs:
//Ovo znači da će objekti ove klase moći da se pokreću u posebnim nitima.
// Svaka nit će obraditi deo posla, analizirati podatke iz fajla, obaviti "lookup" na svakoj
// liniji i zabeležiti rezultat.

public class LookupRunnable implements Runnable {

    private PooledWeblog log;  //referenca na glavni log sistem
    private final List<String> entries;  //lista svih linija koje cekaju na obradu

    public LookupRunnable(PooledWeblog log) {
        this.log = log;

        this.entries = log.getEntries();

    }

    @Override
    public void run() {
        //daj mi sledeci posao
        try {
            for(String entry = this.getNextEntry(); entry != null; entry = this. getNextEntry()) {
                //analiziraj i vrarti rezultat
                String result = this.analyzeEntryAndGetResult(entry);
                if (result == null)
                    continue;

                try {
                    //kad dobijemo rezultat logujemo ga
                    //Rezultat obrade se upisuje u izlazni stream (log fajl), ako je analiza uspešna.
                    this.log.log(result);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private String getNextEntry() throws InterruptedException {
        //MONITOR - osigurava da samo jedna nit u isto vreme manipulise listom
        synchronized (this.entries) {

            //DOHVATICE SLEDECI POSAO ZA OBRADU IZ OVOG REDA , A AKO NEMA VISE POSLOVA VRACA NULL

            //ako je prazna lista  (spavas/probudis se) nit c spavati dok neko drugi ne doda nove zad
            while (this.entries.size() == 0) {
                //ako je lista prazna nit proverava da li je obrada zavrsena
                if (this.log.isFinished()) {
                    System.err.println("Thread exiting..." + Thread.currentThread());
                    return null;
                }

                //ako nije posao gotov to znaci da je lista prazna ali bice ubaceno nesto nekad
                //cekamo

                //kada uradimo wait nad entries cekamo da neko da signal
                //onaj kojio ubacuej posloce kad iaci posao uradi sinal ako neko ceka da zna da moze da uzme
                //da bismo radili wit moramo da budemo u monitoru
                this.entries.wait();
            }

            //daj mi prvi sa vrha
            return this.entries.remove(0);
        }

    }

    private  String analyzeEntryAndGetResult(String entry){

        //ON UZIMA KONKRETAN POSAO (JEDNA LINIJ AFAJLA) I ANALIZIRACE STA TREBA DA URADI
        //za datu linij afajla radi loookeup
        //TODO
        //vratice null ako  se desila neka greska (NIJE LINIJA ISPRAVNO PARSIRANA ILI NISO USPELI LOOKUP DA URADIMO))

        //trazimi prci razmak jer je on posle ip adrese npr 192.168.1.1 ____ [datum] "jsdaskld"
        int index = entry.indexOf(' ');
        if(index == -1)
            return  null;

        //izdvaja host od pocetka do indeks sto znai da ce izdvojiti 192.168.1.1
        String host = entry.substring(0, index);
        try{
            //getByName - ako je host = www.google.com onda vraca www.google.com/142.250.184.196 (i ip addr)
            //          - a ako je host IP adresa onda vraca samo IP adresu
            // zato kada dodamo getHostName - u slucaju da je ost IP adresa sa getHostName dovijamo host name
            // npr www.google.com a ako je host vec ostr name samo vrac ahost name
            return InetAddress.getByName(host).getHostName();
        } catch (UnknownHostException e) {
            return host;
        }


    }
}

//Rezime:
//Ovaj kod implementira multithreaded obrada log fajla pomoću radnih niti koje uzimaju zadatke
// iz zajedničke liste, analiziraju ih (izvode "lookup" na IP adresu) i rezultat zapisuje.
// Synchronized blokovi omogućavaju sigurnu manipulaciju sa zajedničkom listom zadataka,
// a wait() i notify() omogućavaju efikasno upravljanje radnim nitima koje čekaju na nove zadatke.
