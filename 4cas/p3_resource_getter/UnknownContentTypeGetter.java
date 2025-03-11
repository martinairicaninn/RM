package p3_resource_getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

public class UnknownContentTypeGetter  {

    private static final String URL_STRING = "https://helloacm.com/api/random/?n=128";

    public static void main(String[] args) throws MalformedURLException {


        try{
            URL u = new URL(URL_STRING);
            //getContet daje instancu neke klase
            /*
            u.getContent().getClass().getName() služi da otkrije tačan tip objekta koji metoda getContent() vraća.
            📌 Ova informacija pomaže da znaš kako dalje da obradiš preuzeti sadržaj (kao tekst, sliku,
             bajtove itd.).
             */
            System.out.println("I got a: " + u.getContent().getClass().getName());

            //pravimo niz tipova koje ocekujemo
            Class<?>[] types = new Class[3];
            types[0] = String.class;       //ako je obican tekst
            types[1] = Reader.class;      // ako je tekstualni sadrzaj u obliku streama (HTML, JSON, XML)
            types[2]= InputStream.class;  //ako je binarni moze ga dati kao inputstream (slka, PDF, MP3)
            //vraca nam kontent imajuci oce tipove u vidu (prvo sta uhvati)
            Object o = u.getContent(types);

            //provera redom da li j ovaj content koji je vracen kao objekat neka od novih klasa za koju si rekao
            //da podrzavas i vraca prvu koja uspe

            //provera sta je uhvatio
            if(o instanceof String) {
                System.out.println("STRING");
                System.out.println(o);

            }else if (o instanceof Reader){
                System.out.println("READER");
                //ispisujemo kao karaktere
                int c;
                Reader r = (Reader) o;
                while((c = r.read()) != -1)
                    System.out.print((char)c);
                r.close();
                //citamo bajtove to je binarni fajhl
            }else if(o instanceof InputStream){
                System.out.println("INPUT STREAM");
                int c;
                InputStream in = (InputStream) o;
                while((c = in.read()) != -1)
                    System.out.write(c);
                in.close();
            }else{
                System.out.println("Error: unexpectred type" + o.getClass());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
