package p2_bank;

import java.util.concurrent.ThreadLocalRandom;

//Stvara instancu TransferRunnable – zadatak za nit koji:
//Radi transfer sa jednog računa na drugi.
//Prenosi maksimalno 10 jedinica novca po transakciji.

/*
U Javi, Runnable je interfejs koji se koristi za definisanje zadatka koji će se izvršavati u
posebnoj niti (thread).

 Ključna stvar kod Runnable interfejsa je da on sadrži samo jednu metodu --> void run()
 */

public class TransferRunnable implements  Runnable{

    //Konstanta: Maksimalno vreme čekanja između transfera, izraženo u milisekundama.
    static final int MAX_TRASFER_DELAY = 1;

    /*
    bank: Referenca na objekat banke koji implementira IBank interfejs. Ovaj objekat se koristi za
     pozivanje metode transfer koja obavlja transfer novca.
     */
    private final IBank bank;

    /*
    from:  Indeks računa sa kojeg će biti izvršen transfer. Svaka instanca TransferRunnable
    predstavlja transfer sa jednog specifičnog računa.
     */
    private final int from;

    //maksimalni iznos novca koji se moze preneti u jednom transferu
    private final int max;

    TransferRunnable(IBank bank, int from, int max){
        this.bank = bank;
        this.from = from;
        this.max = max;

    }
    @Override
    public void run() {
        /*
        Kada imamo više niti koje koriste nasumične brojeve, umesto Random koristimo ThreadLocalRandom
        da bismo izbegli probleme sa sinhronizacijom i poboljšali performanse.

        U tvom kodu, on omogućava da svaka nit nasumično bira račune i količinu novca za transfer
         */

        //pravimo generator slucajnoh brojeva koji je bezbedan za rad sa vise niti
        //ThreadLocalRandom.current(); → Pribavlja generator koji je vezan samo za trenutnu nit.
        ThreadLocalRandom r = ThreadLocalRandom.current();
        //u beskonacnoj petlji radimo transfer na koji racun cemo da prosledimo novac
        try {
            while(true) {
                //pravimo transfer sa jednog racuna na neki nasumicni

                //broj racuna u banci koji se nasumicno bira
                //nasumican broj izmedju 0 i bank.count
                int to = r.nextInt(bank.count());
                //koliko prosledjujemo (nasumicno se bira i iznos koji ce se preneti)
                int amount = r.nextInt(this.max);
                //poziva se metoda transer koja ce izvriti transfer
                this.bank.transfer(this.from, to, amount);

                //Pauza između dva uzastopna transfera. Koristi se Thread.sleep kako bi nit sačekala nasumičan interval pre sledećeg transfera.
                //MAX_TRASFER_DELAY je postavljen na 1, pa su kašnjenja vrlo kratka.
                Thread.sleep(r.nextLong(MAX_TRASFER_DELAY));
            }


        } catch (InterruptedException e) {
                    throw new RuntimeException(e);
        }
    }

}

