package store;

import channel.handler.ChannelHandler;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by timmytime on 18/02/15.
 */
public class ChannelStore {


    public Map<String, ChannelHandler> channelMap = new HashMap<String, ChannelHandler>();


    public ChannelStore() {

    }

    public void updateChannel(String key, ChannelHandler channel) {
        channelMap.put(key, channel);
    }

    public ChannelHandler addChannel(String key, Channel channel) {

        ChannelHandler channelHandler = new ChannelHandler(channel);

        channelMap.put(key, channelHandler);

        return channelHandler;
    }

    public void removeChannel(String key) {
        channelMap.remove(key);
    }

    public ChannelHandler getChannel(String key) {
        return channelMap.get(key);
    }

    public void stop() {
        channelMap.clear();
    }

}
