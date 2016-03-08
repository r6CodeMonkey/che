package server;

import core.HazelcastManager;
import core.HazelcastManagerInterface;
import util.Configuration;
import util.TopicPair;
import util.TopicSubscriptions;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;

/**
 * Created by timmytime on 12/12/15.
 */
public class HazelcastServer extends UnicastRemoteObject implements HazelcastManagerInterface {

    private final HazelcastManager hazelcastManager = new HazelcastManager();

    public HazelcastServer() throws RemoteException {
        super(0);
        HazelcastManager.start();
    }

    public static void stopServer() {
        HazelcastManager.stop();
    }

    public static void startServer() throws Exception {
        Configuration configuration = new Configuration();

        try {
            LocateRegistry.createRegistry(configuration.getPort());
        } catch (Exception e) {

        }

        HazelcastServer server = new HazelcastServer();
        Naming.rebind(configuration.getURL(), server);

        //add a shut down hook.  mainly for testing / local development.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                HazelcastManager.stop();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        startServer();
    }

    @Override
    public void createTopic(String topic) {
        hazelcastManager.createTopic(topic);
    }

    @Override
    public String subscribe(String topic, String key) {
        return hazelcastManager.subscribe(topic, key);
    }

    @Override
    public void bulkSubscribe(List<TopicPair> topicPairs) throws RemoteException {
        hazelcastManager.bulkSubscribe(topicPairs);
    }

    @Override
    public void unSubscribe(String topic, TopicSubscriptions topicSubscriptions) {
        hazelcastManager.unSubscribe(topic, topicSubscriptions);
    }

    @Override
    public void bulkUnSubscribe(List<TopicPair> topicPairs) throws RemoteException {
        hazelcastManager.bulkUnSubscribe(topicPairs);
    }

    @Override
    public void publish(String topic, String message) {
        hazelcastManager.publish(topic, message);
    }

    @Override
    public void createMap(String map) {
        hazelcastManager.createMap(map);
    }

    @Override
    public void put(String map, String key, Object object) {
        hazelcastManager.put(map, key, object);
    }

    @Override
    public Object get(String map, String key) {
        return hazelcastManager.get(map, key);
    }

    @Override
    public Collection<?> getAvailableKeys(String map) {
        return hazelcastManager.getAvailableKeys(map);
    }


    @Override
    public void removeAll(String map) {
        hazelcastManager.removeAll(map);
    }

    @Override
    public void remove(String map, String key) {
        hazelcastManager.remove(map, key);
    }

    @Override
    public void addCallback(CheCallbackInterface callbackInterface) {
        hazelcastManager.addCallback(callbackInterface);
    }

}
