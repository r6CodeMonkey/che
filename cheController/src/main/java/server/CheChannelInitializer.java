package server;

import channel.CheHandler;
import channel.JsonFrameDecoder;
import channel.JsonHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import util.Configuration;

/**
 * Created by timmytime on 12/12/15.
 */
public class CheChannelInitializer extends ChannelInitializer {

    public static final String JSON_FRAMER_HANDLER = "jsonFramer";
    public static final String STRING_DECODER_HANDLER = "stringDecoder";
    public static final String STRING_ENCODER_HANDLER = "stringEncoder";
    public static final String JSON_HANDLER = "jsonHandler";
    public static final String CHE_HANDLER = "cheHandler";


    private final CheHandler cheHandler;
    private final Configuration configuration;

    public CheChannelInitializer(Configuration configuration) {
        this.configuration = configuration;
        cheHandler = new CheHandler(configuration);
    }


    @Override
    protected void initChannel(Channel channel) throws Exception {
        //    channel.pipeline().addLast(OBJECT_DECODER, new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())));
        channel.pipeline().addLast(JSON_FRAMER_HANDLER, new JsonFrameDecoder());
        channel.pipeline().addLast(STRING_DECODER_HANDLER, new StringDecoder());
        channel.pipeline().addLast(STRING_ENCODER_HANDLER, new StringEncoder());
        channel.pipeline().addLast(JSON_HANDLER, new JsonHandler(configuration));
        channel.pipeline().addLast(CHE_HANDLER, cheHandler);
    }

    public void stop() {
        //nothing to do for this at present.
    }


}
