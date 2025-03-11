package p01_intro;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class SSLSocketsIntro {


    @SuppressWarnings({ "null", "unused" })
    public static void main(String[] args) {

        // samo u svrhu demostracije
        SSLSocket socket = null;

        // Dobij podržane šifre (cipher suites) koje se koriste za šifrovanje, od sledećeg:
        // SSL_RSA_WITH_RC4_128_MD5
        // SSL_RSA_WITH_RC4_128_SHA
        // TLS_RSA_WITH_AES_128_CBC_SHA
        // TLS_DHE_RSA_WITH_AES_128_CBC_SHA
        // TLS_DHE_DSS_WITH_AES_128_CBC_SHA
        // SSL_RSA_WITH_3DES_EDE_CBC_SHA
        // SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA
        // SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA
        // SSL_RSA_WITH_DES_CBC_SHA
        // SSL_DHE_RSA_WITH_DES_CBC_SHA
        // SSL_DHE_DSS_WITH_DES_CBC_SHA
        // SSL_RSA_EXPORT_WITH_RC4_40_MD5
        // SSL_RSA_EXPORT_WITH_DES40_CBC_SHA
        // SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA
        // SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA
        // SSL_RSA_WITH_NULL_MD5
        // SSL_RSA_WITH_NULL_SHA
        // SSL_DH_anon_WITH_RC4_128_MD5
        // TLS_DH_anon_WITH_AES_128_CBC_SHA
        // SSL_DH_anon_WITH_3DES_EDE_CBC_SHA
        // SSL_DH_anon_WITH_DES_CBC_SHA
        // SSL_DH_anon_EXPORT_WITH_RC4_40_MD5
        // SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA
        String[] supportedCipherSuits = socket.getSupportedCipherSuites();

        //Dobij omogućene šifre (cipher suites) koje se koriste za šifrovanje.
        String[] enabledCipherSuites = socket.getEnabledCipherSuites();

		/*
		Interfejs HandshakeCompletedListener nam omogućava da primamo obaveštenja o završetku SSL protokolnog handshake-a na određenoj SSL vezi.
        Kada se SSL handshake završi, uspostavljeni su novi bezbednosni parametri.
        Ti parametri uvek uključuju bezbednosne ključeve koji se koriste za zaštitu poruka.
        Takođe, mogu uključivati i parametre povezane sa novom sesijom, kao što su autentifikovani identitet partnera i nova SSL šifra (cipher suite).
		*/
        class HandshakeInterfaceExample implements HandshakeCompletedListener {

            @Override
            public void handshakeCompleted(HandshakeCompletedEvent e) {
                SSLSession session = e.getSession();
                session.getProtocol();
                session.getPeerHost();

                // Handshake completion logic goes here, some useful methods on the event object:
                e.getSession();
                e.getSocket();
                e.getCipherSuite();
            }

        }

    }

}
