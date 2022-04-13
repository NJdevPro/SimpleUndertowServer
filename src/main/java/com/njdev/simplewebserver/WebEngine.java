package com.njdev.simplewebserver;

import io.undertow.Undertow;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;

public abstract class WebEngine {

    static final String KEY_STORE_PATH = System.getProperty("user.dir") + "/src/main/resources/httpsKeyStore";

    public static SSLContext sslContext(String keystoreFile, String password)
            throws GeneralSecurityException, IOException {
        var keystore = KeyStore.getInstance(new File(keystoreFile), password.toCharArray());
        var keyMgrFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyMgrFactory.init(keystore, password.toCharArray());

        var trustMgrFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustMgrFactory.init(keystore);

        var sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyMgrFactory.getKeyManagers(), trustMgrFactory.getTrustManagers(), new SecureRandom());

        return sslContext;
    }

    public static void main(String... args) {

        var restServer = new RestServer();
        try {
            SSLContext sslContext = sslContext(KEY_STORE_PATH + "/keystore.p12","password");

            Undertow server = Undertow.builder()
                    .addHttpListener(8080, "localhost", restServer.initHandlers())
                    .addHttpsListener(8443, "localhost", sslContext, restServer.initHandlers())
                    .build();

            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
