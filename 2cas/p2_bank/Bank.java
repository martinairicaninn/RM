package p2_bank;

import java.lang.reflect.Array;
import java.util.Arrays;

/*
Poenta ovog koda je da implementira banka sa više računa koja omogućava transfer novca između tih računa.
 */

public class Bank implements  IBank{

    //banka ce da cuva sve racune u jednom nizu
    private final int[] accounts;
    //accountsNum: Broj računa u banci.
    //initialBalance: Početni iznos koji svaki račun ima.
    Bank(int accountsNum, int initialBalance){
        //Kreira niz accounts sa veličinom accountsNum.
        this.accounts = new int[accountsNum];
        //popunjava sve racune sa pocetnim iznosom
        Arrays.fill(this.accounts, initialBalance);
    }




    //amount je kolicina novca koja se salje
    @Override
    public void transfer(int from, int to, int amount) {

        //proveravamod a li na racunu sa kojeg se salje novac ima dovoljno novca
        if(this.accounts[from]< amount)
            return;

        //ako ima dovoljno novca uradi transfer

        //koja od niti trenutno radi transfer
        System.out.println(Thread.currentThread());

        //kako radimo transfer
        this.accounts[from] -= amount;
        this.accounts[to] += amount;
        System.out.printf("Transfer from %2d to %2d: %4d\n", from, to, amount);

        //nakon svakog transfera ispisemo koliko ima novca u banci
        System.out.println("Ttal balance: " + this.getTotalBalance());

    }

    @Override
    public int count() {
        return this.accounts.length;
    }

    @Override
    public int getTotalBalance() {
        //vraca sumu svih accounta
        //kreira strim od celobrojnog niza accounts koji cuva stanje svih bankovnih racuna
        return  Arrays.stream(this.accounts).sum();
    }
}
