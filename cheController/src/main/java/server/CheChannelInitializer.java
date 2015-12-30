package server;

import channel.CheHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import util.Configuration;

/**
 * Created by timmytime on 12/12/15.
 */
public class CheChannelInitializer extends ChannelInitializer {

    public static final String OBJECT_ENCODER = "objectEncoder";
    public static final String OBJECT_DECODER = "objectDecoder";
    public static final String CHE_HANDLER = "cheHandler";


    private Configuration configuration;
    private CheHandler cheHandler;

    public void init(Configuration configuration) {
        this.configuration = configuration;

        cheHandler = new CheHandler(configuration);
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        //   channel.pipeline().addLast(OBJECT_ENCODER, new ObjectEncoder());
        //   channel.pipeline().addLast(OBJECT_DECODER, new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())));
        channel.pipeline().addLast("decode", new StringDecoder());
        channel.pipeline().addLast("encode", new StringEncoder());

        channel.pipeline().addLast(CHE_HANDLER, cheHandler);
    }

    public void stop() {
        //nothing to do for this at present.
    }


}
