package core;

import channel.CheControllerHandler;
import channel.CheHandler;
import channel.JsonFrameDecoder;
import channel.JsonHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;
import util.Configuration;
import util.security.SSLConfiguration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * Created by timmytime on 09/12/15.
 */
public class NettyChannelInitializer extends ChannelInitializer {

    public static final String SSL_HANDLER = "sslHandler";
    public static final String JSON_FRAMER_HANDLER = "jsonFramer";
    public static final String STRING_DECODER_HANDLER = "stringDecoder";
    public static final String STRING_ENCODER_HANDLER = "stringEncoder";
    public static final String JSON_HANDLER = "jsonHandler";
    public static final String CHE_HANDLER = "cheHandler";
    public static final String CHE_CONTROLLER_HANDLER = "cheControllerHandler";


    private final Configuration configuration;
    private final CheControllerHandler cheControllerHandler;
    private SSLContext sslContext;

    public NettyChannelInitializer(Configuration configuration) throws Exception {
        this.configuration = configuration;
        this.cheControllerHandler = new CheControllerHandler(configuration);

        try {
            sslContext = SSLConfiguration.configure(configuration);
        } catch (Exception e) {
            configuration.getLogger().error("SSL Context Configuration failed " + e.toString());
        }
    }


    @Override
    protected void initChannel(Channel channel) throws Exception {

        if (sslContext != null) {
            SSLEngine engine = sslContext.createSSLEngine();
            engine.setUseClientMode(false);
            channel.pipeline().addLast(SSL_HANDLER, new SslHandler(engine));
        }
        channel.pipeline().addLast(JSON_FRAMER_HANDLER, new JsonFrameDecoder());
        channel.pipeline().addLast(STRING_DECODER_HANDLER, new StringDecoder());
        channel.pipeline().addLast(STRING_ENCODER_HANDLER, new StringEncoder());
        channel.pipeline().addLast(JSON_HANDLER, new JsonHandler(configuration));
        channel.pipeline().addLast(CHE_HANDLER, new CheHandler(configuration));
        channel.pipeline().addLast(CHE_CONTROLLER_HANDLER, cheControllerHandler);
    }

    public void stop() {
        //nothing to do for this at present.
    }

}

