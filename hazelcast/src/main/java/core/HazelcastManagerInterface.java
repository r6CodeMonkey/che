package core;

import com.hazelcast.core.IMap;
import model.server.TopicSubscriptions;
import server.CheCallbackInterface;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by timmytime on 12/12/15.
 */
public interface HazelcastManagerInterface extends Remote {

     void createTopic(String topic) throws RemoteException;

     String subscribe(String topic, String key) throws RemoteException;

     void unSubscribe(String topic, TopicSubscriptions topicSubscriptions) throws RemoteException;

     void publish(String topic, Object message) throws RemoteException;

     void createMap(String map) throws RemoteException;

     void put(String map, String key, Object object) throws RemoteException;

     Object get(String map, String key) throws RemoteException;

     IMap get(String map) throws RemoteException;

     void removeAll(String map) throws RemoteException;

     void remove(String map, String key) throws RemoteException;

     void addCallback(CheCallbackInterface callbackInterface) throws RemoteException;

}
