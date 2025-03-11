package p1_intro;

/*
Kreira i pokreće dve različite niti:
MyThread (nasleđuje Thread).
MyThreadRunnable (implementira Runnable).
Svaka nit:
Čeka 5 sekundi (Thread.sleep(5000)).
Ispisuje svoj ID niti (Thread.currentThread().getId()).
Glavna nit (main) čeka da se prva nit (t) završi pomoću join().
Na kraju, main ispisuje "Finishing main...".

 */

public class Main {

    //MyThread nasledjuje Thread
    static class MyThread extends  Thread{
        @Override
        //run se izvrsava kada se nit pokrene
        public void run() {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getId());
        }
    }

    static class MyThreadRunnable implements  Runnable{

        @Override
        public void run() {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getId());
        }
    }

    public static void main(String[] args){
        System.out.println(Thread.currentThread().getId());
        //pokrece niti
        Thread t = new MyThread();
        t.start();

        //pokretanje niti preko runnable-a
        new Thread(new MyThreadRunnable()).start();

        //ukoliko zelimo da se prvo sve niti zavrse pa onda Finishing main
        //join cekad a odgovarajuca nit zavrsi sa radom
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Finishing main...");

    }
}
