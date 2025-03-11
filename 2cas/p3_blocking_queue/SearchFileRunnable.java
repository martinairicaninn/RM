package p3_blocking_queue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

/*
Ova klasa je zadužena za pretragu fajlova u direktorijumu (koji se unose iz reda) u potrazi za
određenom ključnom rečju. Klasa implementira Runnable, što znači da je dizajnirana da se pokrene kao nit.
 */

public class SearchFileRunnable  implements  Runnable{

    //koristi se za komunikaciju izmedju razlicitih niti
    //red omogucava asinhrono uzimanje i stavljanje fajlova u red
    private final BlockingQueue<Path> queue;

    //kljucna rec koja ce biti trazena u fajlovima
    private final String keyword;

    SearchFileRunnable(BlockingQueue<Path> queue, String keyword){
        this.queue = queue;
        this.keyword = keyword;

    }

    /*
    Primer rada sa više niti
Zamislimo da imamo 3 niti (SearchFileRunnable) i sledeće stanje reda:

queue = [file1, file2, file3, END_OF_WORK]

Prva nit (T1) uzima file1, obrađuje ga.

Druga nit (T2) uzima file2, obrađuje ga.

Treća nit (T3) uzima file3, obrađuje ga.

Sada u redu ostaje samo END_OF_WORK.

T1 uzima END_OF_WORK, shvata da treba da završi:

Pre nego što završi, vraća END_OF_WORK nazad.
T2 uzima END_OF_WORK, shvata da treba da završi, i opet vraća END_OF_WORK.

T3 uzima END_OF_WORK i takođe završava.

Na kraju, sve niti su završile rad.

Zaključak
Ovaj deo koda:

if(p == FileTreeWalkerRunnable.END_OF_WORK){
    this.queue.put(p);
    break;
}
Prepoznaje kraj rada kada naiđe na END_OF_WORK.
Vraća END_OF_WORK u red, kako bi i druge niti mogle da ga preuzmu i završe rad.
Prekida izvršavanje (break;), jer više nema fajlova za obradu.
Ovo je efikasan način sinhronizacije više niti, osigurava da sve niti dobiju signal za kraj rada.

     */


    @Override
    public void run() {
        //sta worker jedan treba da radi
        //uzme fajl iz reda
        try {

            //dok nije posao gotov
            while (true) {
                //queue.take() - za uzimanje fajlova iz reda
                //ako red nema fajlova nit ce biti blokirana dok neki fajl se ne stavi u red
                Path p = this.queue.take();

                //ako nit preuzme fajl koji oznacava kraj reda, nit ce ponovo staviti ovaj fajl u red i
                //zavrsiti svoje izvrsavanje
                //Kada se završi dodavanje fajlova u red, ova nit stavlja END_OF_WORK u red kako bi
                // obavestila sve pretraživačke niti (SearchFileRunnable) da više nema fajlova za obradu.
                if(p == FileTreeWalkerRunnable.END_OF_WORK){
                    this.queue.put(p);
                    break;
                } else {
                    //ako je fajl validan poziva se metod za pretragu tog fajla
                    search(p);
                }

            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.err.println("Thread " + Thread.currentThread() + " stopping...");
    }

    //Ova metoda search(Path f) pretražuje dati fajl f i ispisuje sve linije koje
    // sadrže ključnu reč (this.keyword).
    private void search(Path f){
        try(Scanner sc = new Scanner(f)){
            for(int ln = 1; sc.hasNextLine(); ln++){
                String line = sc.nextLine();
                if(line.contains(this.keyword))
                    System.out.printf("%s:%d\n", f.getFileName(), ln );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
