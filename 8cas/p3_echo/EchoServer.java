package p3_echo;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;



public class EchoServer extends  Thread implements Closeable {

    public static void main(String[] args) throws SocketException {
        new EchoServer(12345).start();
    }

    //imcemo jedna soket koji cemo da koritimo - predstavlja nas sererski soket
    private final DatagramSocket socket;
    public EchoServer(int port) throws SocketException {
        //inicijalizujemo nas serverski soket
        this.socket = new DatagramSocket(port);
    }



    @Override
    public void run() {

        System.err.println("Server starting... ");


        try {
            while (true) {
                byte[] buf = new byte[256];

                //var umesto Datagram
                var request = new DatagramPacket(buf, buf.length);
                this.socket.receive(request);

                //send back echo
                try {
                    //taj bafer koji smo primil isto to saljes od pocetka, nula nije neophodna jer se podrazumeva da salje od pocetka, i citav bafer se salje
                    //request.getLength() je ispravnije od buf.lenght jer bufer moze da bude manji od 256
                    var response = new DatagramPacket(buf, 0, request.getLength(), request.getAddress(), request.getPort());
                    this.socket.send(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //provera ako je poslato end da se prekida tj gasi se server
                //evo ti bufer od pocetka do request.getLength()
                String recived = new String(buf, 0, request.getLength(), StandardCharsets.UTF_8);    //todo
                System.err.println("Server recv: " + recived + " " + Arrays.toString(buf));
                System.out.println(Arrays.toString(recived.getBytes(StandardCharsets.UTF_8)));
                if (recived.equalsIgnoreCase("end")) {
                    System.err.println("Server recived end signal");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.err.println("Server shutting down");
    }

    @Override
    public void close() throws IOException {
        this.socket.close();

    }

}

