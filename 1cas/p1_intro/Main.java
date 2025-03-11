package p1_intro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class Main {
    public static void main(String[] args) {
        //                              Stream(I/O) - close()
        //                                  /          \
        //                          InputStream     OutputStream
        //

        InputStream in;
        OutputStream out;

        try {
            //kreira tok za pisanje podataka u fajl
            FileOutputStream fout = new FileOutputStream("/path/to/file");
            //kreira tok za citanje podataka iz fajla
            FileInputStream fin = new FileInputStream("/path/to/file");

            //komprimitovani fajlovi
            GZIPInputStream gzin = new GZIPInputStream(new FileInputStream("satgod"));

            BufferedInputStream bfin = new BufferedInputStream(fin);
            BufferedOutputStream bfout = new BufferedOutputStream(fout);

            Reader r;
            Writer w;

            InputStreamReader isr = new InputStreamReader(fin,  StandardCharsets.UTF_8);
            OutputStreamWriter osw = new OutputStreamWriter(fout, StandardCharsets.US_ASCII);

            FileReader fr;
            FileWriter fw;

            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            BufferedWriter bw ;

            fout.close();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PrintStream ps = null;
        try {
            //ako je boolean true ovaj bafer koji mi imako ce biti flasovan  kad god upisamo neki bajt niza  ili kad god je
            //printline pozvan ili kad god je novi red ispisan
            ps = new PrintStream(new FileOutputStream("nesto"), true);
            ps.println(2);
            ps.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



    }
}
