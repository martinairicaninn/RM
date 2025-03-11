package p2_protocol_test;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args){

        testProtocol("http://www.adc.org");

        testProtocol("https://www.amazon.com/exec/obidos/order2/");

        //@ nije mail (oznacava putanju)
        testProtocol("mailto:ivan_ristovic@math.rs");

        // File Transfer Protocol
        testProtocol("ftp://metalab.unc.edu/pub/languages/java/javafaq/");

        // SSH
        testProtocol("ssh://mi14007@alas.matf.bg.ac.rs");

        // SSH File Transfer Protocol
        testProtocol("sftp://metalab.unc.edu/pub/languages/java/javafaq/");

        // telnet
        testProtocol("telnet://dibner.poly.edu");

        // Local file access
        testProtocol("file:///etc/passwd");

        // Gopher
        testProtocol("gopher://gopher.anc.org.za/");

        // Lightweight Directory Access Protocol
        testProtocol("ldap://ldap.itd.umich.edu/o=University%20of%20Michigan,c=US?postalAddress");

        // JAR
        testProtocol("jar:http://cafeault.org/books/javaio/ioexamples/javaio.jar!" + "/com/macfaq/io/StreamCopier.class");

        // NFS, Network File System
        testProtocol("nfs://utopia.poly.edu/usr/tmp/");

        // Custom protocol for JDBC
        testProtocol("jdbc:mysql://luna.metalab.unc.edu:3306/NEWS");

        // RMI, a custom protocol for remote method invocation
        testProtocol("rmi://metalab.unc.edu/RenderEngine");


    }

    private static void testProtocol(String url) {

        try {
            URL u = new URL(url);
            System.out.println(u.getProtocol() + ":\tSUPPOTRED");
        } catch (MalformedURLException e) {
            String protocol = url.substring(0, url.indexOf(':'));
            System.out.println(protocol + ":\tNOT PROTOCOL");
        }

    }
}
