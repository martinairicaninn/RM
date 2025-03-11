package p3_resource_getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;

public class SourceReader {

    private static final String URL_STRING = "http://poincare.matf.bg.ac.rs/";

    public static void main(String[] args) throws IOException{

        URL u = new URL(URL_STRING);

        try (BufferedReader in = new BufferedReader(
                //otvaramo inputstream do streama koji daje url
                //kada radimo bez connection-a moramo da znamo koliko citamo
                //BufferReader moze da nam da liniju po liniju teksta i vraca null ako ne procita nista
                new InputStreamReader(u.openStream())
        )){
            String line;
            while((line = in.readLine()) != null)
                //citamo source
                System.out.println(line);

        }catch (IOException e){
             e.printStackTrace();

        }

        System.out.println("==============================================================");

        /*
                    Šta znači conn.getContentEncoding()?
            Kada tražiš podatke sa nekog servera (recimo, preko HTTP-a), server može poslati dodatne informacije
            o tome kako je sadržaj koji ti šalje "kodiran" ili "kompresovan". Te informacije server šalje u tzv.
            HTTP zaglavljima.

            Jedno od tih zaglavlja je Content-Encoding.

            Primer zaglavlja Content-Encoding:
            Ako server šalje fajl kao običan tekst:

            http
            Copy code
            Content-Encoding: UTF-8
            Ovo znači da je sadržaj u UTF-8 formatu (standardni format za tekst).

            Ako server šalje kompresovan sadržaj:

            http
            Copy code
            Content-Encoding: gzip
            Ovo znači da je sadržaj kompresovan pomoću gzip algoritma i moraš ga dekompresovati pre nego što
            ga pročitaš.
         */





        URLConnection conn = u.openConnection();
        String encoding = conn.getContentEncoding();
        if(encoding == null) {
            encoding = "UTF-8";
        }

        /*
        Ovaj deo koda služi za čitanje podataka sa servera preko interneta i pretvara te podatke (koji dolaze
        kao bajtovi) u tekst koristeći zadato enkodiranje. Hajde da detaljno objasnimo svaki deo
         */

        try(BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        //Konvertuje bajtove u čitljiv tekst prema zadatom enkodiranju (ovde encoding, npr. UTF-8).
                        conn.getInputStream(),
                        encoding
                )
        )){

            //isto kada imamo otvorene streamove citamo liniju po liniju
            //imitiramo curl manje vise
            String line;
            while((line = in.readLine()) != null)
                System.out.println(line);

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }


    }

}
