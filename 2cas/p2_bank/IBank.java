package p2_bank;

public interface IBank {
    void transfer(int from, int to, int amount);

    //broj racuna u banci
    int count();

    //vraca intiger koja je ukupna svota
    //Ova metoda vraća ukupnu sumu novca koja se nalazi na svim računima u banci.
    int getTotalBalance();
}

