package p3_echo;

import java.io.IOException;

public class EchoTest {

    //imacemo main metod u kom cemo da pokrenemo client i server kao zasebne aplikacije
    //i client i server ce imati neke metode start ili kako vec, da ne moramod a pokrecemo i jednu i drugu apliikaciju, nego jednim ovim testom
    //  mozemo fa idimo da li klijent i server rade kako treba


    public static void main(String[] args) {
        try(EchoServer server = new EchoServer(12345);
            EchoClient client = new EchoClient("localhost", 12345);)
        {

            //startujemo server da slusa Datagrame
            server.start();


            String echo;
            //client ce da posalje echo, imacemo metod sendEcho i poslace string koji mi napisemo
            //sendEcho ce da posalje Datagram sa ovom niskom i da vrati odg od servera u ovoj nisci 'echo'
            echo = client.sendEcho("(test1) hello!");
            System.out.println("(test1) returned: " + echo);
            echo = client.sendEcho("(test2) works?");
            System.out.println("(test2) returned: " + echo);
            echo = client.sendEcho("(test3)  \uD83D\uDC45");
            System.out.println("(test3) returned: " + echo);


            //ako client serveru posalje 'end' to je kaj da se server ugasi
            client.sendEnd("end");
            Thread.sleep(2000);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Test finished!");
    }


}
