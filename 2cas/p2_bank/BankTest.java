package p2_bank;

//NE MOZEMO SA ISTOG RACUNA ISTOVREMENO DA PREBACIMO NA DVA RAZLICITA RACUNA
//MEDJUTIM NAS POSILJALAC MOZE BITI U NEKOM TRENUTKU PRIMALAC  DOK RADI SVOJ TRANSFER

//ZADATAK DO KRAJA CASA JE DA SINHRONIZUJEMO OVE NASE TRANSFERE KORISTECI RAZLICITE NACINE SINHRONIZACIJE
//ZATO SMO PRAVILI INTERFACE JER CEMO PRAVITI RAZLICITE BANKE
//JEDNA KOJA KORISTIKATANCE, DRUGA KOJA KORISTI SINHRONIZACIJU

import java.awt.datatransfer.Transferable;

public class BankTest {

    static final int ACCOUNTS = 5;
    //promenljiva koja oznacava koliko svaki od tih acounta ima inicijalno novca na svom racunu
    static final int STARTING_BALANCE = 1000000;


    public static void main(String[] args){

       // IBank bank = new Bank(ACCOUNTS, STARTING_BALANCE);
        IBank bank = new LockedBank(ACCOUNTS, STARTING_BALANCE);
        for(int i = 0; i < ACCOUNTS; i++){
            //niti rade transfere svog racuna na neki racun nad bankom
            //Tranfer prima banku, indekst svog racuna tj sa kog racuna ce slati novac i koliko ce max novca da prosledi
            TransferRunnable transfer = new TransferRunnable(bank, i, 10);
            Thread t = new Thread(transfer);
            t.start();

        }
    }

}
