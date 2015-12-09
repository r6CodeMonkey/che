package core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import util.Configuration;

/**
 * Created by timmytime on 09/12/15.
 */
public class NettyChannelInitializer extends ChannelInitializer {

    private Configuration configuration;

    public void init(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {

    }

    public void stop() {
    }

}

