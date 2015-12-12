package core;

import channel.handler.JsonFrameDecoder;
import channel.handler.JsonHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;
import util.Configuration;
import util.SSLConfiguration;

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

    private Configuration configuration;
    private SSLContext sslContext;


    public void init(Configuration configuration) {

        this.configuration = configuration;
        try {
            sslContext = SSLConfiguration.configure(configuration);
        } catch (Exception e) {
             configuration.getLogger().error("SSL Context Configuration failed "+e.toString());
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

    }

    public void stop() {
    }

}

