package p4_echo_service;

import javax.naming.ldap.SortKey;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandlerRunnable implements Runnable{

    private final Socket client;

    public ClientHandlerRunnable(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        //DA BISMO IMPLEMENTIRALI run TREBAJU NAM STRIMOVI koje mozemod a dobijemo od soketa i prosledimo runnableu ili da prosledimo citam soket (sto ovde i radimo) i pravimo
        //kontruktor koji ce taj soket da sascuva negde (EchoServer)

        try(BufferedReader in = new BufferedReader(
                new InputStreamReader(this.client.getInputStream(),
                        StandardCharsets.UTF_8

                )
        );

            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(this.client.getOutputStream(),
                    StandardCharsets.UTF_8
            ))
        ) {

            String line;
            while((line = in.readLine()) != null){
                out.write(line);
                out.newLine();
                out.flush();

            }



        } catch (IOException e) {
            System.err.printf("Clinet handler [%2d] errored: \n", Thread.currentThread().getId());
            e.printStackTrace();

            //zatvaramo klijenta
        } finally {
            try {
                this.client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.err.printf("Client handler [%2d] exited.\n", Thread.currentThread().getId());
    }
}
