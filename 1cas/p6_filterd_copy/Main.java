package p6_filterd_copy;

import java.io.*;
import java.util.Scanner;

//prepisujemo samo vlastite imenice (biloo na klk)(vlastita pocinju velikim slovom)
public class Main {

    public static void main(String[] args){

        try (
                //za citanje podataka iz ulaznog fajla fin.txt
                var in = new Scanner(
                        new InputStreamReader(
                        new BufferedInputStream(
                                new FileInputStream("1cas/p6_filterd_copy/fin.txt")
                        )
                )
                );

                var out = new OutputStreamWriter(
                        new BufferedOutputStream(
                                new FileOutputStream ("1cas/p6_filterd_copy/fout.txt")
                        )
                )
                ) {

            /*
            skeniramo sta citamo
            Scanner sc = new Scanner(in);
            //next ucitava do beline
            sc.next();
            mozemo doati odmah sanner gore u in
            */

            //umesto split - rade istu star
            /*
            Delimitator (\\b) označava granicu reči u regularnim izrazima.
            To znači da će Scanner čitati pojedinačne reči umesto celih linija ili tokena razdvojenih
            razmacima.
             */
            in.useDelimiter("\\b");
            while(in.hasNext()){
                //cita sledecu rec i cuva je u word
                String word = in.next();

                            if(isName(word)) {
                                out.write(word);
                                out.write("\n");
                            }

            }

            System.out.println("Done!");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static boolean isName(String word) {
        //return word.matches("[A-Z][a-z]+");
        if(word.length() < 2)
            return false;

        //ako nije prvo slovo veliko - (word.charAt(0)) vraca prvi karakter reci
        if(!Character.isUpperCase(word.charAt(0)))
            return false;

        //vrati rec u kojoj preskaces prvo slovo i proveravas da li su ostali karakteri sve mala slova
        return word.chars()
                .skip(1)

                .allMatch(c -> Character.isLowerCase(c));
    }


}
