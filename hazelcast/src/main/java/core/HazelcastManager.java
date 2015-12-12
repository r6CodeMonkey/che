package core;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import util.NettyChannelHandler;
import util.TopicSubscriptions;

/**
 * Created by timmytime on 11/12/15.
 */
public class HazelcastManager {

    private static HazelcastInstance hazelcastInstance;

    public static void start(){
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }

    public static void stop(){
        hazelcastInstance.shutdown();
    }

    public static void createTopic(String topic){
        hazelcastInstance.getTopic(topic);
    }

    public static String subscribe(String topic, NettyChannelHandler user ){
       return hazelcastInstance.getTopic(topic).addMessageListener(user);
    }

    public static void unSubscribe(String topic, TopicSubscriptions topicSubscriptions){
        hazelcastInstance.getTopic(topic).removeMessageListener(topicSubscriptions.getSubscription(topic));
        topicSubscriptions.removeSubscription(topic);
    }

    public static void publish(String topic, Object message){
        hazelcastInstance.getTopic(topic).publish(message);
    }

    public static void createMap(String map){
        hazelcastInstance.getMap(map);
    }

    public static void put(String map, String key, Object object){
        hazelcastInstance.getMap(map).put(key, object);
    }

    public static Object get(String map, String key){
        return hazelcastInstance.getMap(map).get(key);
    }

    public static IMap get(String map){
        return hazelcastInstance.getMap(map);
    }

    public static void removeAll(String map){
        hazelcastInstance.getMap(map).evictAll();
    }

    public static void remove(String map, String key){
        hazelcastInstance.getMap(map).remove(key);

    }

}
