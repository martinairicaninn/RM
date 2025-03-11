package p2_copy_image;

import java.io.*;

public class Main {

    //imamo jednu slicicu png i hocemo da je prekopiramo u fajl koji sew zove out.PNG
    public static void main (String[] args) throws IOException {

        try {
            //ODAKLE CITAMO
            //var - kompajler automatski zakljuci tip promenljive na osnovu vrednsoti
            var in = new BufferedInputStream(new FileInputStream("1cas/p2_copy_image/in.PNG"));
            var out = new BufferedOutputStream(new FileOutputStream("1cas/p2_copy_image/out.PNG"));

            //redom ucitavamo bajt po bajt
            //read-vraca int i to ce biti 1 bajt

            /*
            Ovaj kod meri vreme potrebno za kopiranje podataka iz jednog toka (InputStream) u drugi
            (OutputStream) bajt po bajt.
             */
            long start = System.currentTimeMillis();


            int b;
            while(( b= in.read()) != -1)
                //ispisujemo u buff od pocetka onoliko koliko smo procitali
                out.write(b);

            long end = System.currentTimeMillis();
            System.out.println("Finished in: " + (end - start));


            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
