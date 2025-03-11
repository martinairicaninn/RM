package p5_print_streams;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args){

        try {
            PrintStream pw = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream("1cas/p5_print_streams/pw_out.txt")
                    )
            );


            pw.print("Heloo");
            pw.println(" world!");
            pw.println(1);
            pw.println(3.4444);

            float temp = 30.2f;
            pw.printf("Today is %4.2f C.", temp);
            pw.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



    }
}
