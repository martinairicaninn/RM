package p1_chargen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class ChargenClientNonBlocking {

    public static void main(String[] args) {

        String host = "localhost";
        int port = 19000;

        try {
            SocketAddress address = new InetSocketAddress(host, port);
            SocketChannel client = SocketChannel.open(address);


            ByteBuffer buffer = ByteBuffer.allocate(74);
            WritableByteChannel out = Channels.newChannel(System.out);

            //koje god operacije se rade nad kanalom client nece biti blokirane
            client.configureBlocking(false);

            while (true){
                //koliko je bajtova procitao
                int n = client.read(buffer);

                //ako si bilo sta procitao, onda uradi ispis, u suprotn9om citaj u taj isti bafer (ne mora da se procita malo, pa da se resetuje opet za citanje)
                //citaj sve dok ne popunis buffer, pa tek onda akd uradis sve uradi ispisivanje
                if(n > 0){
                    //ako si nesto procitao i nas bafer nema mesta mozemo da ga ispraznimo
                    //ovo Remaining gleda da li ima razlike izmedju nase pozicije i limita tj da li ima mesta
                       /*
                            if(!buffer.hasRemaining()){

                            }
                       */

                    buffer.flip();

                    out.write(buffer);

                    buffer.clear();



                }
                //ako nije nista procitao
                if(n == -1){
                    // ...
                    break;
                    //ako si procitoa 0 bajtova mozes uraditi neki drugi posao u medjuvremenu
                } else {

                    //do some work

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
