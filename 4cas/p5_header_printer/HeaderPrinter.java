package p5_header_printer;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

//PRAVIMO PROGRAM KOJI CEW DA URADI HEAD REQUEST

public class HeaderPrinter {



    public static void main(String[] args){

        //sa tsandardnog ulaza se ucitava linij apo linija (te linije predstavljau URL-ove)
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            try{
                String line = sc.nextLine();
                //preskace prazne linije
                if(line.trim().equals(""))
                    continue;
                //cita dok ne dodje do exita
                if(line.equalsIgnoreCase("exit"))
                    break;

                //kreiramo URL za svaku od linija
                URL u = new URL(line);
                URLConnection conn = u.openConnection();

                // u kontrolnoj tabli  crlu -I (sajt)
                //ako cemo pomocui  mape
                /*
                KEY :        VALUE(String)
                Date: Sat, 14 Dec 2024 16:51:24 GMT
                Server: Apache/2.2.13 (Unix) mod_ssl/2.2.13 OpenSSL/0.9.8k DAV/2 PHP/5.2.10
                Last-Modified: Thu, 06 Apr 2023 09:46:55 GMT
                ETag: "4c566-14af-5f8a7c9f421c0"
                Accept-Ranges: bytes
                Cache-Control: max-age=2592000
                Content-Type: image/gif
                Content-Length: 5295
                 */


                System.out.println("------------------------------");

                //zbog prvog koji je null
                //vraca prvi header koji je http status linija
                System.out.println(conn.getHeaderField(0));

                for(int i = 1; ; i++){
                    //getHeaderFiled - dohvati vrednost i-tog headera
                    String value = conn.getHeaderField(i);
                    if(value == null)
                        break;

                    //dohvata ime i-tog headewra (Date, Contet-Type, itd..)
                    String key = conn.getHeaderFieldKey(i);

                    System.out.println(key + " " + value);
                }

                System.out.println("------------------------------");





            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        sc.close();
    }
}
