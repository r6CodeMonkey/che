package factory;

import io.netty.channel.Channel;
import message.CheMessage;
import message.HazelcastMessage;
import model.CheChannel;
import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by timmytime on 30/12/15.
 */
public class CheChannelFactory {

    private static Map<String, CheChannel> cheChannelMap = new HashMap<>();

    public static CheChannel getCheChannel(String key) {
        return cheChannelMap.get(key);
    }

    private static void addCheChannel(String key, Channel channel) {
        cheChannelMap.put(key, new CheChannel(channel));
    }

    public static void removeCheChannel(String key) {
        cheChannelMap.remove(key);
    }

    public static void updateCheChannel(String key, Channel channel) {
        if (cheChannelMap.containsKey(key)) {
            if(!cheChannelMap.get(key).getChannel().isActive() || !cheChannelMap.get(key).getChannel().isOpen()) {
                cheChannelMap.get(key).updateChannel(channel);
            }
        } else {
            addCheChannel(key, channel);
        }
    }

    public static void write(String key, HazelcastMessage cheMessage) throws JSONException, NoSuchAlgorithmException {
        cheChannelMap.get(key).write(cheMessage);
    }

    public static void write(String key, CheMessage cheMessage) throws JSONException, NoSuchAlgorithmException {
        cheChannelMap.get(key).write(cheMessage);
    }

    public static void force(String key, Channel channel){
         updateCheChannel(key, channel);
        //and now we force the next messages waiting.
        cheChannelMap.get(key).force();
    }

}
