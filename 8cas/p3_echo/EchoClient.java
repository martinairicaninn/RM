package p3_echo;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EchoClient implements Closeable {

    public static void main(String[] args) throws IOException {
        var client = new EchoClient("localhost", 12345);
        System.out.println(client.sendEcho("hello"));
        System.out.println(client.sendEcho("hello2"));
        System.out.println(client.sendEcho("hello3"));


    }

    //immao datagramsoket
    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    public EchoClient(String hostname, int port) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(10000);
        this.address = InetAddress.getByName(hostname);
        this.port = port;

    }

    @Override
    public void close() throws IOException {

        this.socket.close();
    }


    //sendEcho treba da vrati response od servera
    public String sendEcho(String msg) throws IOException {

        /* mi saljemo sserveru poruku (msg) enkodiranu u bajtove, tako sto prebacamo u bajt niz odmah

         */

        byte[] buf = msg.getBytes(StandardCharsets.UTF_8);
        System.err.println("Client send: " + msg + " " + Arrays.toString(buf));  //Arrays.toString(buf) ispis bajt niza

        //send request
       DatagramPacket request = new DatagramPacket(buf, buf.length, this.address, this.port);
       this.socket.send(request);

       //recive response
        DatagramPacket response = new DatagramPacket(buf, buf.length);
        this.socket.receive(response);
        System.err.println("Client recv: " + Arrays.toString(response.getData()));


        //return parsed echo
        return new String(response.getData(), 0, response.getLength(),  StandardCharsets.UTF_8);  //response.getData()  je taj bajt niz


    }

    public void sendEnd(String end) {
    }
}
