package p4_zip;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class Main {

    public static void main(String[] args) throws IOException {

        try {
            var in = new BufferedInputStream(new FileInputStream("1cas/p4_zip/in.txt"));
            var out = new BufferedOutputStream(
                    new GZIPOutputStream(
                            new FileOutputStream("1cas/p4_zip/out.gz")
                    )
            );

            long start = System.currentTimeMillis();

            int b;
            while ((b = in.read()) != -1)
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
