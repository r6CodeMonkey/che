package util;

import com.hazelcast.core.MessageListener;

import io.netty.channel.Channel;

/**
 * Created by timmytime on 11/12/15.
 */
public class NettyChannelHandler implements MessageListener {

    private Channel channel;

    public NettyChannelHandler(Channel channel){
        setChannel(channel);
    }

    public Channel getChannel(){
        return channel;
    }

    public void setChannel(Channel channel){
        this.channel = channel;
    }

    public boolean isChannelOpen(){
        return channel.isOpen();
    }

    public void onMessage(com.hazelcast.core.Message message) {
      //more to do here.
        channel.writeAndFlush(message);
    }
}
