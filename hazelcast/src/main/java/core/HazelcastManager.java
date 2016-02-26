package core;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import server.CheCallbackInterface;
import util.CheMessageHandler;
import util.TopicSubscriptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Created by timmytime on 11/12/15.
 */
public class HazelcastManager implements HazelcastManagerInterface {

    private static HazelcastInstance hazelcastInstance;
    private static CheCallbackInterface cheCallbackInterface;

    public static void start() {

        //need to consider config....but the test is stupid.  ie 10,000 topics published in 1 second.  lol
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }

    public static void stop() {
        hazelcastInstance.shutdown();
    }

    public void createTopic(String topic) {
        hazelcastInstance.getTopic(topic);
    }

    public String subscribe(String topic, String key) {
        return hazelcastInstance.getTopic(topic).addMessageListener(new CheMessageHandler(cheCallbackInterface, key));
    }

    public void unSubscribe(String topic, TopicSubscriptions topicSubscriptions) {
        hazelcastInstance.getTopic(topic).removeMessageListener(topicSubscriptions.getSubscription(topic));
        topicSubscriptions.removeSubscription(topic);
    }

    public void publish(String topic, String message) {

        new Thread(() -> {
            hazelcastInstance.getTopic(topic).publish(message);
        }).start();
    }

    public void createMap(String map) {
        hazelcastInstance.getMap(map);
    }

    public void put(String map, String key, Object object) {
        hazelcastInstance.getMap(map).put(key, object);
    }

    public Object get(String map, String key) {
        return hazelcastInstance.getMap(map).get(key);
    }

    @Override
    public Collection<?> getAvailableKeys(String map) {

        IMap iMap = hazelcastInstance.getMap(map);

        List<String> keys = new ArrayList<>();

        if (iMap == null) {
            return keys;
        }

        Set<String> subUtmKeys = iMap.keySet();

        for (String key : subUtmKeys) {
            keys.add(key);
        }

        return keys;
    }


    //this is not serializable...so basically need to remove it.  and do something else...ha ha
    public IMap get(String map) {
        return hazelcastInstance.getMap(map);
    }

    public void removeAll(String map) {
        hazelcastInstance.getMap(map).evictAll();
    }

    public void remove(String map, String key) {
        hazelcastInstance.getMap(map).remove(key);
    }

    @Override
    public void addCallback(CheCallbackInterface callbackInterface) {
        this.cheCallbackInterface = callbackInterface;
    }


}
