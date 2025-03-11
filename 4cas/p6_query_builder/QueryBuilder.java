package p6_query_builder;


//Construts a GET request URL by concatenating base URL and query key-value pairs
//Konstruiše URL GET zahteva tako što spaja osnovnu URL adresu i parove ključ-vrednost upita
//BASE_URL?key1=value1&key2=value2& .... &keyn=valuen

// QueryBuilder.java – pomoćna klasa koja kreira URL sa GET parametrima.

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class QueryBuilder {

    private final StringBuilder query;


    /*
    QueryBuilder konstruktor prima jedan argument, endpoint
    (osnovni URL, na primer https://api.example.com/data).
     */
    public QueryBuilder(String endpoint) {
        this.query = new StringBuilder(endpoint); //endpoint je pocetni string (BASE_URL)
        this.query.append('?');
    }

    public QueryBuilder append(String key, String value){  //appendujemo key i value

        this.query.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
        this.query.append('=');
        this.query.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
        this.query.append('&');
       /* this.query.append(key).append('=').append(value).append('&');// na query apendujemo
        //vracamo this jer mozemo da appendujemo dalje, da mozemod a appendujemo na string builder novu stvar

        */
        return this;
    }


    /*
    Ova metoda jednostavno vraća string koji predstavlja URL sa svim dodanim parametrima.
    toString() je metoda koja je automatski pozvana kada se objekat QueryBuilder koristi kao string.
      Dakle, toString() ovde koristiš da bi dobio URL kao običan string koji se može koristiti u daljim
        koracima.
     */

    @Override
    public String toString() {
       return this.query.toString();
    }


    public URL toUrl() throws MalformedURLException {
        return new URL(this.toString());
    }
}
