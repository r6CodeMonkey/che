package util;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by timmytime on 12/12/15.
 */
public class SSLConfiguration {

    private static KeyStore loadKeyStore(String format, String path, char[] password) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, FileNotFoundException, IOException {

        KeyStore keyStore = KeyStore.getInstance(format);

        try (InputStream inputStream = new FileInputStream(path)) {
            keyStore.load(inputStream, password);
        }

        return keyStore;
    }

    public static SSLContext configure(Configuration configuration) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {


        TrustManager[] managers = null;

        //if we want to use a truststore can correct this
      //  KeyStore trustStore = loadKeyStore(configuration.getTrustStoreFormat(), configuration.getTrustStore(), configuration.getTrustStorePassword().toCharArray());

     //   TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
     //   tmf.init(trustStore);
     //   managers = tmf.getTrustManagers();

        KeyStore keyStore = loadKeyStore(configuration.getKeyStoreFormat(), configuration.getKeyStore(), configuration.getKeyStorePassword().toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, configuration.getKeyStorePassword().toCharArray());

        SSLContext context = SSLContext.getInstance(configuration.getSslProtocol());
        context.init(kmf.getKeyManagers(), managers, null);

        return context;
    }
}
