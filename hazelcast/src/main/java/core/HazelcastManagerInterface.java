package core;

import server.CheCallbackInterface;
import util.TopicPair;
import util.TopicSubscriptions;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

/**
 * Created by timmytime on 12/12/15.
 */
public interface HazelcastManagerInterface extends Remote {

    void createTopic(String topic) throws RemoteException;

    String subscribe(String topic, String key) throws RemoteException;

    void bulkSubscribe(List<TopicPair> topicPairs) throws RemoteException;

    void unSubscribe(String topic, TopicSubscriptions topicSubscriptions) throws RemoteException;

    void bulkUnSubscribe(List<TopicPair> topicPairs) throws RemoteException;

    void publish(String topic, String message) throws RemoteException;

    void bulkPublish(String topic, List<String> messages) throws RemoteException;

    void createMap(String map) throws RemoteException;

    void put(String map, String key, Object object) throws RemoteException;

    Object get(String map, String key) throws RemoteException;

    Collection<?> getAvailableKeys(String map) throws RemoteException;

    void removeAll(String map) throws RemoteException;

    void remove(String map, String key) throws RemoteException;

    void addCallback(CheCallbackInterface callbackInterface) throws RemoteException;

}
