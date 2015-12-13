package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import util.Configuration;

/**
 * Created by timmytime on 12/12/15.
 */
public class CheChannelInitializer extends ChannelInitializer {

    public static final String OBJECT_ENCODER = "objectEncoder";
    public static final String OBJECT_DECODER = "objectDecoder";

    private Configuration configuration;

    public void init(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(OBJECT_ENCODER, new ObjectEncoder());
        channel.pipeline().addLast(OBJECT_DECODER, new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())));
    }

    public void stop() {
        //nothing to do for this at present.
    }


}
