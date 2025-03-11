package p3_resource_getter;

//Opis programa:
//Preuzimanje binarnog fajla

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BinaryFileDownloader {

    public static void main(String[] args){

        try {
            URL url = new URL("http://www.matf.bg.ac.rs/images/matf.gif");

            //citamo iz streama
                  // InputStream in = url.openStream();
            //petlja iz koje citamo sa streama
            //u Contetnt-Lenght iz komandne linije vidimo koliko imamo bajtova da procitamo (5295) i te
            //informacij eimamo kada otvorimo URLConnection

            URLConnection conn = url.openConnection();

            //kupimo info
            /*
            Šta radi? Dobija tip sadržaja koji se preuzima (npr. "image/gif", "text/html", "application/pdf").
                Primeri vrednosti:
                "text/html" → HTML stranica
                "image/png" → PNG slika
                "application/json" → JSON podaci
             */
            String contetntType = conn.getContentType();
            int contetntLength = conn.getContentLength();
            /*
            Šta radi? Vraća putanju iz URL-a (bez domena).
                Primeri:
                "http://example.com/images/photo.jpg" → /images/photo.jpg
                "https://matf.bg.ac.rs/index.html" → /index.html
             */
            String path = url.getPath(); //vraca ceo path od/images... a nas zanima samo matf.gif
            // indexOf uzima prvo pojavljivanje, a lastIndexOf poslednje
            String filename = path.substring(path.lastIndexOf('/') + 1);


            //ako pocinje sa textom to je textualni fajl i to se ne preuzima jer nam treba binarni
            if(contetntLength == -1  || contetntType.startsWith("text")){
                throw new UnsupportedOperationException("Content is not a binary file.");
            }

            //provera da l smo lepo ucitali filename
            System.out.println("Downloading " + filename + " ....");

            //koristimo BufferdInputStream a ne reader jer nije tekstualni fajl
            try (BufferedInputStream in = new BufferedInputStream(
                    conn.getInputStream()

            );
                 //trebace nam nesto za ispis jer hocemo da pisemo u neki fajl
                 BufferedOutputStream out = new BufferedOutputStream(
                         new FileOutputStream(filename)   //filename ime fajla u koji pisemo
                 )
            ){

                //imamo streamove sad prelazimo na citanje
                for(int i = 0; i < contetntLength; i++ ){
                    int b = in.read();
                    out.write(b);
                }


            }

            System.out.println("Finished!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
