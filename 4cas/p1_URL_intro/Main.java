package p1_URL_intro;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main {
    public static void main(String[] args) throws MalformedURLException {

        // URL - za identifikaciju resursa [URI]
        // http - protokol aplikativnog sloja
        // protocol://userInfo@host:30(port)/path/to/index.html?query#fragment  (nece uvek imati svi delovi)
        // port - kada imamo nas racunar i kad stigne nesto nama kojoj aplikacijio to pripada
        //      - kada napravimo serversku aplikaciju vezemo je za odgovarajuci port
        //

        // URL url = new URL("http://www.matf.bg.ac.rs");

        URL url = new URL("http://userInfo@host:30(port)/path/to/index.html?query#fragment");
        try {

            //NACIN KAKO MOZEMO DA OTVORIMO STREAM DO RESAURSA I DA GA PREUZMEMO
            //openStream forsira konekciju hosta, trazi taj resurs i vraca stream do tog resursa
            //Omogućava čitanje podataka direktno sa interneta kao da su datoteka.
            InputStream in = url.openStream();
            //citamo ovaj resurs (fakticki peuzmemo ga sa weba)
            in.read();

            //isto kao i ovo gore
            URLConnection conn = url.openConnection();
            conn.getContentLength(); //duzina niza u bajtovima
            //sve sto moze da bude u headerima  mozemo da pristupimo preko URLConnection
            /*
                Ova linija je ekvivalent url.openStream(), ali preko URLConnection.
                ✅ Omogućava napredniju manipulaciju, kao što je dodavanje HTTP headera ili podešavanje
                    timeout-a.
             */
            InputStream cin = conn.getInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
