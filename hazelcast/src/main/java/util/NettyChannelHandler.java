package util;

import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import io.netty.channel.Channel;

/**
 * Created by timmytime on 11/12/15.
 */
public class NettyChannelHandler implements MessageListener {

    private Channel channel;
    private Message lastMessage;

    public NettyChannelHandler(Channel channel) {
        setChannel(channel);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isChannelOpen() {
        return channel.isOpen();
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void onMessage(Message message) {
        //more to do here.
        lastMessage = message;
        channel.writeAndFlush(message);
    }
}
