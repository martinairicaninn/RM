package p3_daytime_service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class DaytimeClient {

    public static void main(String[] args) {

        String host = "localhost";

        System.err.println("Connecting to " + host);

        //povezivanje na server
        try (Socket client = new Socket(host, DaytimeServer.PORT)) {

            System.err.println("Connected!");

            try(BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream())
               )){

                String time = in.readLine();
                System.out.println("It is " + time + "@" + host + ":" + DaytimeServer.PORT);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
