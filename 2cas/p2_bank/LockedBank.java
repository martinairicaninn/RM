package p2_bank;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Ovaj pristup se koristi kako bi se izbegle trke i moguće greške u konkurentnim situacijama.

final class LockedBank implements IBank {

    //cuva stanje svih racuna u banci
    private final int[] accounts;
    //za zasittu kriticnih sekcija koda da bi se zastitili podaci kada vise niti pokusava da pristupi
    //istim podacima istovremeno
    private final Lock lock;

    //Condition je objekat koji omogucava da nit ceka dok ne dobije signal da moze da nastavi
    // insufficientFunds - omogucava da nit saceka ako na racunu nema dovoljno novca, umesto da prekine
    // operaciu
        /*
        Koristi se zajedno sa lock-om:
            await() – Nit se uspavljuje dok ne dobije signal da može da nastavi.
            signalAll() – Obaveštava sve niti koje čekaju da provere da li sada imaju dovoljno sredstava
            za transfer.
         */
    private final Condition insufficientFunds;

    LockedBank(int accountsNum, int initialBalance){
        this.accounts = new int[accountsNum];
        Arrays.fill(this.accounts, initialBalance);

        //Kreira ReentrantLock objekat koji omogućava ručno zaključavanje i otključavanje kritičnih sekcija koda.
        this.lock = new ReentrantLock();

        /*
        this.insufficientFunds = this.lock.newCondition();
Kreira uslov (Condition) povezan sa lock-om.
Čemu služi?
Ako nit želi da izvrši transfer, ali nema dovoljno novca, čekaće na ovom uslovu (await()).
Kada se izvrši neki transfer i stanje računa se promeni, obavestiće druge niti da ponovo provere da li
sada mogu da nastave (signalAll()).
         */
        this.insufficientFunds = this.lock.newCondition();
    }




    @Override
    public void transfer(int from, int to, int amount) {

        //zakljucava kriticnu sekciju - sprecava druge niti da istovremeno izvrse transfer
        this.lock.lock();
        //ukupno kesa na racunu posle transfera
        int totalBalanceAfterTransfer;
        try{
            //dok nema dovoljno novca na racunu cekaj da se uradi transfer
            while(this.accounts[from]< amount) {
                try {
                    //atomicki uzmes lock i da nastavis dalje
                    //svi koji cekaju, dobice signal, atomicno ce da hvataju katanac i da nastavljaju dalje
                    //proveravaju da li sada imaju dovoljno, ako nemaju, cekaju opet, dok se ne izvrsi neki transfer
                    this.insufficientFunds.await();
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
            this.insufficientFunds.signalAll();

            /*
            Uvek otključava zaključavanje kako bi omogućila drugim nitima pristup.

              Čak i ako dođe do greške, finally osigurava da se lock.unlock() uvek pozove.
             */
        } finally{
            this.lock.unlock();
        }




        System.out.printf("Transfer from %2d to %2d: %4d\n", from, to, amount);

        //nakon svakog transfera ispisemo koliko ima novca u banci
        System.err.println("Total balance: " + totalBalanceAfterTransfer);



    }

    @Override
    public int count() {
        return this.accounts.length;
    }

    @Override
    public int getTotalBalance() {
        //vraca sumu svih accounta
        return  Arrays.stream(this.accounts).sum();
    }
}
