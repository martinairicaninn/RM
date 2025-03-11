package p3_blocking_queue;

//PRAVIMO PRPGRAM KOJI CE KADA UNESEMO DIREKTORIJUM I KLJUCNU REC
// PR. UNESEMO DIREKTORIJUM 2cas i program ide rekurzivno koroz direktorijum i za svaki fajl na koji naidje
// pretrazi ga i ispise sva pojavljivanja kljucne reci "keyword"
//i kljucna ec sae zadaje sa ulaza

//ideja: ZA SVAKI FAJL OTVORIMO THREAD I RADIMO PRETRAGU

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class SearchMain {

    private static final int FILE_QUEUE_SIZE = 10;
    private static final int THREADS_NUM = 5;

    public static void main(String[] args){

        //omogucava unos podataka sa st.ulaza
        var sc = new Scanner(System.in);
        System.out.println("Enter base dir:");
        //koji direkttrijum je pocetni unosu se
        String dir = sc.nextLine();
        System.out.println("Enter keyword:");
        String keyword = sc.nextLine();
        sc.close();


        /*
        BlockingQueue<Path> – Red koji podržava operacije blokiranja kada je red pun (za put()) ili
                               prazan (za take()).
        ArrayBlockingQueue<>(FILE_QUEUE_SIZE) – Fiksna veličina reda (FILE_QUEUE_SIZE), tj. najviše
                                               FILE_QUEUE_SIZE fajlova može biti u redu istovremeno.
Svrha: Ovaj red koristi se za komunikaciju između niti, gde jedna nit dodaje fajlove (direktorijumi),
       a druge ih preuzimaju i pretražuju.
         */



        BlockingQueue<Path> fileQueue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

        //pretraga direktorijuma, prolazi kroz direktorijuma (Paths.get(dir)) i stavlja putanje u fajlove
                //fileQueue
        var ftw = new FileTreeWalkerRunnable(Paths.get(dir), fileQueue);
        //pokretanje niti
        new Thread((Runnable) ftw).start();

        /*
             ^^^^^^
             Ovaj kod pokreće jednu nit koja pretražuje direktorijume i ubacuje fajlove u red.
              Kasnije će drugi SearchFileRunnable radnici (worker threads) preuzimati fajlove iz reda
              i pretraživati ključnu reč.
         */


        //start workers
        for(int i = 0; i < THREADS_NUM; i++)
            //                                            odakle,    sta
            new Thread((Runnable) new SearchFileRunnable(fileQueue, keyword)).start();
    }
}
