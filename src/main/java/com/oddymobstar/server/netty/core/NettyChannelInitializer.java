package com.oddymobstar.server.netty.core;

import com.oddymobstar.server.netty.channel.handler.*;
import com.oddymobstar.server.netty.store.HazelcastManager;
import com.oddymobstar.server.netty.util.Configuration;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

//import io.netty.channel.codec.json.JsonObjectDecoder;

/**
 * Created by timmytime on 04/02/15.
 */
public class NettyChannelInitializer extends ChannelInitializer {


    public static final String SSL_HANDLER = "sslHandler";
    public static final String STRING_DECODER_HANDLER = "stringDecoder";
    public static final String STRING_ENCODER_HANDLER = "stringEncoder";
    public static final String JSON_HANDLER = "jsonHandler";
    public static final String CORE_MESSAGE_HANDLER = "coreMessageHandler";
    public static final String PACKAGE_MESSAGE_HANDLER = "packageMessageHandler";
    public static final String ALLIANCE_MESSAGE_HANDLER = "allianceMessageHandler";
    public static final String GRID_MESSAGE_HANDLER = "gridMessageHandler";
    public static final String JSON_FRAMER_HANDLER = "jsonFramer";

    private HazelcastManager hazelcastManager;
    private CoreMessageHandler coreMessageHandler;
    private PackageMessageHandler packageMessageHandler;
    private AllianceMessageHandler allianceMessageHandler;
    private GridMessageHandler gridMessageHandler;

    private SSLContext sslContext;

    private Configuration configuration;

    public void init(Configuration configuration) {

        this.configuration = configuration;

        try {
            sslContext = initSSLEngine(configuration);
        } catch (Exception e) {

        }

        hazelcastManager = new HazelcastManager();
        coreMessageHandler = new CoreMessageHandler(configuration, hazelcastManager);
        packageMessageHandler = new PackageMessageHandler(configuration, hazelcastManager);
        allianceMessageHandler = new AllianceMessageHandler(configuration, hazelcastManager);
        gridMessageHandler = new GridMessageHandler(configuration, hazelcastManager);


    }


    public void stop() {
        hazelcastManager.stop();
    }


    @Override
    protected void initChannel(Channel channel) throws Exception {

        if (sslContext != null) {
            SSLEngine engine = sslContext.createSSLEngine();
            engine.setUseClientMode(false);
            channel.pipeline().addLast(SSL_HANDLER, new SslHandler(engine));
        }


        // channel.pipeline().addLast(new SslHandler())
        //the order is very important lol.
        //  channel.pipeline().addLast(STRING_DECODER_HANDLER, new StringDecoder());
        //  channel.pipeline().addLast(STRING_ENCODER_HANDLER, new StringEncoder());
        channel.pipeline().addLast(JSON_FRAMER_HANDLER, new JsonFrameDecoder());
        channel.pipeline().addLast(STRING_DECODER_HANDLER, new StringDecoder());
        channel.pipeline().addLast(STRING_ENCODER_HANDLER, new StringEncoder());
        channel.pipeline().addLast(JSON_HANDLER, new JsonHandler(configuration));
        channel.pipeline().addLast(CORE_MESSAGE_HANDLER, coreMessageHandler);
        channel.pipeline().addLast(GRID_MESSAGE_HANDLER, gridMessageHandler);
        channel.pipeline().addLast(PACKAGE_MESSAGE_HANDLER, packageMessageHandler);
        channel.pipeline().addLast(ALLIANCE_MESSAGE_HANDLER, allianceMessageHandler);


    }

    private SSLContext initSSLEngine(Configuration configuration) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, KeyManagementException, IOException, UnrecoverableKeyException {

        SSLContext context = null;

        TrustManager[] managers = null;

        KeyStore trustStore = KeyStore.getInstance(configuration.getTrustStoreFormat());
        KeyStore keyStore = KeyStore.getInstance(configuration.getKeyStoreFormat());

        InputStream trustStream = null;
        InputStream keyStream = null;

        //use with resources. TODO
        try {
            trustStream = loadStore(configuration.getTrustStore());
        } catch (FileNotFoundException fnfe) {

        }

        try {
            keyStream = loadStore(configuration.getKeyStore());
        } catch (FileNotFoundException fnfe) {

        }

        if (trustStream != null) {
            trustStore.load(trustStream, configuration.getTrustStorePassword().toCharArray());
        }

        if (keyStream != null) {
            keyStore.load(keyStream, configuration.getKeyStorePassword().toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        managers = tmf.getTrustManagers();

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, configuration.getKeyStorePassword().toCharArray());

        context = SSLContext.getInstance(configuration.getSslProtocol());
        context.init(kmf.getKeyManagers(), managers, null);

        return context;

    }

    private InputStream loadStore(String path) throws FileNotFoundException {
        InputStream is = new FileInputStream(path);
        return is;
    }


}
