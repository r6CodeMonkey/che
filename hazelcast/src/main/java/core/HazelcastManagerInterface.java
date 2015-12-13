package core;

import com.hazelcast.core.IMap;
import util.NettyChannelHandler;
import util.TopicSubscriptions;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by timmytime on 12/12/15.
 */
public interface HazelcastManagerInterface extends Remote {

    public void createTopic(String topic) throws RemoteException;
    public  String subscribe(String topic, NettyChannelHandler user) throws RemoteException;
    public  void unSubscribe(String topic, TopicSubscriptions topicSubscriptions) throws RemoteException ;
    public  void publish(String topic, Object message) throws RemoteException;
    public  void createMap(String map) throws RemoteException;
    public  void put(String map, String key, Object object) throws RemoteException;
    public  Object get(String map, String key) throws RemoteException;
    public  IMap get(String map) throws RemoteException;
    public  void removeAll(String map) throws RemoteException;
    public  void remove(String map, String key) throws RemoteException;

}
