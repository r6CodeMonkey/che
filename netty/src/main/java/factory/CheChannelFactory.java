package factory;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by timmytime on 30/12/15.
 */
public class CheChannelFactory {

    /*
      need to create an object for this, that can persist messages / and update the channels..
     */


    private final Map<String, Channel> channelMap = new HashMap<>();

    public void addChannel(String uid, Channel channel) {
        channelMap.put(uid, channel);
    }

    public void removeChannel(String uid) {
        channelMap.remove(uid);
    }

    public Channel getChannel(String uid) {
        return channelMap.get(uid);
    }

    public Map<String, Channel> getChannelMap() {
        return channelMap;
    }

}
