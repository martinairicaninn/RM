package p3_copy_text;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        try {
            var in = new BufferedInputStream(new FileInputStream("1cas/p3_copy_text/in.txt"));
            var out = new BufferedOutputStream(new FileOutputStream("1cas/p3_copy_text/out.txt"));

            long start = System.currentTimeMillis();

            int b;

            while((b = in.read()) != -1)
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
