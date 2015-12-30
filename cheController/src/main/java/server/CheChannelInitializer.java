package server;

import channel.CheHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import model.Core;
import util.Configuration;

/**
 * Created by timmytime on 12/12/15.
 */
public class CheChannelInitializer extends ChannelInitializer {

    public static final String OBJECT_ENCODER = "objectEncoder";
    public static final String OBJECT_DECODER = "objectDecoder";
    public static final String CHE_HANDLER = "cheHandler";


    private CheHandler cheHandler;

    public void init(Configuration configuration) {
        cheHandler = new CheHandler(configuration);
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
          channel.pipeline().addLast(OBJECT_ENCODER, new ObjectEncoder());
          channel.pipeline().addLast(OBJECT_DECODER, new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())));
          channel.pipeline().addLast(CHE_HANDLER, cheHandler);
    }

    public void stop() {
        //nothing to do for this at present.
    }


}
