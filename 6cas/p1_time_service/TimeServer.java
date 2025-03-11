package p1_time_service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TimeServer {

    public static void main(String[] args) {


        //razlika u peohama imdeju 1900 i 1970 u sekundama (ovo je 70 god u sekundama)
        long differenceInEpochsSeconds = 2208988800L;

        //pravimo soket koji slusa na serveru

        try (ServerSocket server = new ServerSocket(373737)) {

            //cekamo klijente

            while(true){
                //primamo klijenta
                try (Socket client = server.accept()){
                    //obradjujemo klijenta tako sto dohvatimo trenutno vreme
                    Date now = new Date();

                    //konverzija
                    long msScience1970 = now.getTime();
                    long sScience1970 = msScience1970 / 1000;
                    long sScience1900 = sScience1970 + differenceInEpochsSeconds;


                    //[ ][ ][ ][ ][1][2][3][4]
                    byte[] time = new byte[4];
                    time[0] = (byte) (sScience1900 >> 24);
                    time[1] = (byte) (sScience1900 >> 16);
                    time[2] = (byte) (sScience1900 >> 8);
                    time[3] = (byte) (sScience1900);


                    //slanje klijentu
                    OutputStream out = new BufferedOutputStream((client.getOutputStream()));
                    out.write(time);
                    out.flush();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
