package core;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import model.Player;
import server.CheCallbackInterface;
import util.*;

import java.rmi.RemoteException;
import java.util.*;


/**
 * Created by timmytime on 11/12/15.
 */
public class HazelcastManager implements HazelcastManagerInterface {

    private static HazelcastInstance hazelcastInstance;
    private static CheCallbackInterface cheCallbackInterface;
    private static Configuration configuration;

    public static void start(Configuration config) {

        //need to consider config....but the test is stupid.  ie 10,000 topics published in 1 second.  lol
        hazelcastInstance = Hazelcast.newHazelcastInstance();
        configuration = config;
    }

    public static void stop() {
        hazelcastInstance.shutdown();
    }

    public void createTopic(String topic) {
        hazelcastInstance.getTopic(topic);
    }

    public String subscribe(String topic, String ownerKey, String key) {

        if(topic.trim().isEmpty()){
            return "";
        }

        Player player = (Player)this.get(Tags.PLAYER_MAP, key);

        if(player.getTopicSubscriptions().getKeySet() == null || !player.getTopicSubscriptions().getKeySet().contains(topic)) {
            configuration.getLogger().debug("subscribing too " + topic + " with key " + key);
            return hazelcastInstance.getTopic(topic).addMessageListener(new CheMessageHandler(cheCallbackInterface, key));
        }

        return ""; //we dont add blanks.
    }

    @Override
    public void bulkSubscribe(List<TopicPair> topicPairs) throws RemoteException {

        configuration.getLogger().debug("bulk subscribe");

        topicPairs.parallelStream().forEach(topicPair ->
                        subscribe(topicPair.getTopicKey(),topicPair.getOwnerKey(),  topicPair.getKey())
        );

        topicPairs.parallelStream().forEach(topicPair -> publish(topicPair.getTopicKey(), topicPair.getMessage())
        );

    }

    public void unSubscribe(String topic,String ownerKey, String key) {
        Player player = (Player)this.get(Tags.PLAYER_MAP, key);

            configuration.getLogger().debug("unsubscribing from " + topic + " with key " + key);

            if (player.getTopicSubscriptions().getKeySet() != null) {
                if (player.getTopicSubscriptions().getSubscription(topic) != null && player.getTopicSubscriptions().getSubscription(topic).containsKey(ownerKey)) {

                    hazelcastInstance.getTopic(topic).removeMessageListener(player.getTopicSubscriptions().getSubscription(topic).get(ownerKey));
                    player.getTopicSubscriptions().removeSubscription(topic);

                    put(Tags.PLAYER_MAP, player.getKey(), player);
                }
            }
    }

    @Override
    public void bulkUnSubscribe(List<TopicPair> topicPairs) throws RemoteException {

        configuration.getLogger().debug("bulk unsubscribe");

        topicPairs.parallelStream().forEach(topicPair -> publish(topicPair.getTopicKey(), topicPair.getMessage())
        );

        topicPairs.parallelStream().forEach(topicPair ->
                        unSubscribe(topicPair.getTopicKey(), topicPair.getOwnerKey(), topicPair.getKey())
        );

    }

    public void publish(String topic, String message) {
        configuration.getLogger().debug("publish " + message + " topic is " + topic);
        new Thread(() -> {
            hazelcastInstance.getTopic(topic).publish(message);
        }).start();
    }

    public void bulkPublish(String topic, List<String> messages) throws RemoteException {

        for (String message : messages) {
            publish(topic, message);
        }

    }


    public void createMap(String map) {
        hazelcastInstance.getMap(map);
    }

    public void put(String map, String key, Object object) {
        hazelcastInstance.getMap(map).put(key, object);
    }

    public Object get(String map, String key) {
        configuration.getLogger().debug("get map "+map+" get key "+ key);
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
