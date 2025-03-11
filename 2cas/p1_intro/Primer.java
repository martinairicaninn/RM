package p1_intro;

public class Primer {
/*
Varijabla x je deljena između svih niti. Svaka nit uvećava ovu vrednost LIMIT puta.
 */

    static int x = 0;
    static final int LIMIT = 10000000;
    //br niti
    static final  int TH_NUM = 10;

    static class MyThread implements Runnable{

        @Override
        public void run() {
            //uvecavamo promenljivu
            for(int i = 0; i < LIMIT; i++){
                x++;
            }
        }
    }

    public static void main(String[] args){

        //pokrecemo nit
        for(int i = 0; i < TH_NUM; i++)
            new Thread(new MyThread()).start();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //koliko je ocekivano da x bude u ovom trenutku
        System.out.println("Expected: " + TH_NUM * LIMIT);
        System.out.println("Actual: " + x);
    }
}
