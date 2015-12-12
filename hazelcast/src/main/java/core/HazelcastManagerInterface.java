package core;

import com.hazelcast.core.IMap;
import util.NettyChannelHandler;
import util.TopicSubscriptions;

import java.rmi.Remote;

/**
 * Created by timmytime on 12/12/15.
 */
public interface HazelcastManagerInterface extends Remote {

    public void createTopic(String topic);
    public  String subscribe(String topic, NettyChannelHandler user);
    public  void unSubscribe(String topic, TopicSubscriptions topicSubscriptions) ;
    public  void publish(String topic, Object message);
    public  void createMap(String map);
    public  void put(String map, String key, Object object);
    public  Object get(String map, String key);
    public  IMap get(String map);
    public  void removeAll(String map) ;
    public  void remove(String map, String key);

}
