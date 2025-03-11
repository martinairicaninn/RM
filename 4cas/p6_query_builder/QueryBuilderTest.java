package p6_query_builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

//API
//QueryBuilderTest.java – glavna klasa, pokreće program

public class QueryBuilderTest {

    public static void main(String[] args){

        //nasumican br(id vica)
        int jokeId = new Random().nextInt(500) + 1;
        //                                                      query parametre lepimo

        /*
                https://icanhazdadjoke.com/123/
                (gde je 123 nasumično izabrani vic ID).
         */
        String endpoint = "https://icanhazdadjoke.com/" + jokeId + "/";

        QueryBuilder qb = new QueryBuilder(endpoint);

        try(Scanner sc = new Scanner(System.in)){
            System.out.println("Enter first name:");
            qb.append("firstName", sc.nextLine());
            System.out.println("Enter Last name:");
            qb.append("lastName", sc.nextLine());
        }

        System.err.println("Sending GET request...");
        System.err.println(qb);

       /*
                Šalje HTTP GET zahtev i čita odgovor
                Program sada šalje zahtev na ovaj URL i dobija JSON odgovor:
        */

        try {

            /*
            Dakle, qb.toUrl() kreira i vraća potpuni URL koji sadrži osnovnu URL adresu
            (https://icanhazdadjoke.com/1234) i dodatne parametre koje smo prethodno dodali putem append(
             metode (npr. firstName=John i lastName=Doe).
             */
            URL u = qb.toUrl();

            String json;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            u.openStream()
                    )
            )) {
                json = in.readLine();
            }

            System.err.println(json);

            int jokeFieldPos = json.indexOf("joke\":\"") + 7; // "joke": "Ivan  - krecemo od I zato je + 8
            String joke = json.substring(jokeFieldPos, json.indexOf("\"", jokeFieldPos)); // hocemp " ali od jokeFieldPosition
                 System.out.println(joke);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*

    h   ttps://icanhazdadjoke.com/123/?firstName=John&lastName=Doe
        Šalje HTTP zahtev na taj URL i dobija odgovor:

    {"id":"123","joke":"John, why don’t skeletons fight each other? They don’t have the guts!","status":200}
👉 Ekstraktuje samo vic i prikazuje ga:

            John, why don’t skeletons fight each other? They don’t have the guts!
     */



}
