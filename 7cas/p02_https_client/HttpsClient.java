package p02_https_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

final class HttpsClient {

    public static void main(String[] args) {
        String host = "example.com";
        int port = 443; // Default https port

        try {
            SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket)factory.createSocket(host, port);

            // dobijamo sve koji su podrzani
            String[] supported = socket.getSupportedCipherSuites();
            socket.setEnabledCipherSuites(supported); //svi koji su podrzani nnjih ukljucujemo
            Writer out = new OutputStreamWriter(socket.getOutputStream());

            // HTTPS zahteva punu URL adresu u prvom HTTP zaglavlju
            // \r\n je CRLF (novi red) nakon HTTP zaglavlja
            out.write("GET https://" + host + "/ HTTP/1.1\r\n");
            out.write("Host: " + host + "\r\n");
            out.write("Accept: text/plain\r\n");
            out.write("\r\n");
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("--- begin response --- ");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("--- end response --- ");

            out.close();
            in.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}