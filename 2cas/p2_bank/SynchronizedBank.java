package p2_bank;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//NEMAMO NISTA OD LOCKOVA JER SYNCHRONIZED BLOCK (MONITORI) NAM DAJU OVAJ KONCEPT CEKANJA NA SIGNAL AUTOMATSKI
/*
Korišćenje synchronized ključne reči omogućava automatsko upravljanje zaključavanjem (lock) i
 signalizacijom (signal) između niti, što čini kod jednostavnijim, ali manje fleksibilnim od
  prethodne implementacije (LockedBank).
 */
final class SynchronizedBank implements IBank {
    private final int[] accounts;


    SynchronizedBank(int accountsNum, int initialBalance){
        this.accounts = new int[accountsNum];
        Arrays.fill(this.accounts, initialBalance);


    }


/*
synchronized: Korišćenje ključne reči synchronized pre metode znači da samo jedna nit može izvršiti
ovu metodu u isto vreme na objektu SynchronizedBank. To automatski garantuje da neće doći do
problema sa konkurentnim pristupom (race conditions).
 */

    @Override
    public synchronized void transfer(int from, int to, int amount) {

        int totalBalanceAfterTransfer;
            //dok nema dovoljno novca na racunu cekaj da se uradi transfer
            while(this.accounts[from]< amount) {
                try {

                    this.wait();
                    // TODO wait on signal...

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            //koja od niti trenutno radi transfer
            System.out.println(Thread.currentThread());

            //kako radimo transfer

            this.accounts[from] -= amount;
            this.accounts[to] += amount;
            totalBalanceAfterTransfer = this.getTotalBalance();

            //sve niti koje cekaju na this objekat bice obavestene putem notifyAll()
        //signalizira svim cekajucim nitima da ponovo provere uslov
            this.notifyAll();
            // TODO signal



        System.out.printf("Transfer from %2d to %2d: %4d\n", from, to, amount);

        //nakon svakog transfera ispisemo koliko ima novca u banci
        System.err.println("Total balance: " + totalBalanceAfterTransfer);



    }

    @Override
    public synchronized int count() {
        return this.accounts.length;
    }

    @Override
    public synchronized int getTotalBalance() {
        //vraca sumu svih accounta
        return  Arrays.stream(this.accounts).sum();
    }
}
