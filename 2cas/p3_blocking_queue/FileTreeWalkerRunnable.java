package p3_blocking_queue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

//Ova klasa FileTreeWalkerRunnable prolazi kroz direktorijum i njegove poddirektorijume i dodaje sve
// fajlove u red (BlockingQueue<Path>), kako bi ih druge niti kasnije pretraživale po ključnoj reči.

public class FileTreeWalkerRunnable implements  Runnable {

    //ovo oznacava kraj pretrage, tj kada vise nema fajlova za obradu
    static final Path END_OF_WORK = Paths.get("");

    private final BlockingQueue<Path> queue;

    //pocetni direktorijum
    private final Path startingDir;

    public FileTreeWalkerRunnable(Path path, BlockingQueue<Path> fileQueue) {
        this.startingDir = path;
        this.queue = fileQueue;
    }


    @Override
    public void run() {

        try {
            //setnja kroz pocetni direktorijum
            this.walk(this.startingDir);
            //kada zavrsimo pretragu ubacimio u queue prazan path
            this.queue.put(END_OF_WORK);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void walk(Path startingDir) throws InterruptedException {

        // Files.newDirectoryStream(startingDir) - vraca listu svih fajlova i poddirektorijuma koji se
        //                                         nalaze u njemu
        //try - automatski zatvara direktorijum kada zavrsi sa njim
        try (var ds = Files.newDirectoryStream(startingDir)) {
            //za svaku putanju u streamu proveravamo
            for (Path p : ds) {
                if (Files.isDirectory(p))
                    /*
                    Ako je p direktorijum (Files.isDirectory(p)), rekurzivno pozivamo walk(p).
                    Tako pretražujemo sve poddirektorijume dok ne dođemo do kraja.
                     */
                    walk(p);
                else
                    //ako nije direktorijum znaci fajl i dodajemo ga u red
                    this.queue.put(p);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

