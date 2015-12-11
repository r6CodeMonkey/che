package core;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import util.NettyChannelHandler;

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

    //may not be required
    public static void createTopic(String topic){
        hazelcastInstance.getTopic(topic);
    }

    public static String subscribe(String topic, NettyChannelHandler user ){
       return hazelcastInstance.getTopic(topic).addMessageListener(user);
    }

    public static void unSubscribe(String topic, String regId){
        hazelcastInstance.getTopic(topic).removeMessageListener(regId);
    }

    public static void publish(String topic, Object message){
        hazelcastInstance.getTopic(topic).publish(message);
    }

    public static void createMap(String map){
        hazelcastInstance.getMap(map);
    }

    public static void mapAdd(String map, String key, Object object){
        hazelcastInstance.getMap(map).put(key, object);

    }

    public static void mapRemoveAll(String map){
        hazelcastInstance.getMap(map).evictAll();
    }

    public static void mapRemove(String map, String key){
        hazelcastInstance.getMap(map).remove(key);

    }

}
