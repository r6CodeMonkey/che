package server;

import com.hazelcast.core.IMap;
import core.HazelcastManager;
import core.HazelcastManagerInterface;
import util.Configuration;
import util.NettyChannelHandler;
import util.TopicSubscriptions;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by timmytime on 12/12/15.
 */
public class HazelcastServer extends UnicastRemoteObject implements HazelcastManagerInterface {

    private final HazelcastManager hazelcastManager = new HazelcastManager();

    public HazelcastServer() throws RemoteException {
        super(0);
        HazelcastManager.start();
    }

    @Override
    public void createTopic(String topic) {
        hazelcastManager.createTopic(topic);
    }

    @Override
    public String subscribe(String topic, NettyChannelHandler user) {
        return hazelcastManager.subscribe(topic, user);
    }

    @Override
    public void unSubscribe(String topic, TopicSubscriptions topicSubscriptions) {
        hazelcastManager.unSubscribe(topic, topicSubscriptions);
    }

    @Override
    public void publish(String topic, Object message) {
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
    public IMap get(String map) {
        return hazelcastManager.get(map);
    }

    @Override
    public void removeAll(String map) {
        hazelcastManager.removeAll(map);
    }

    @Override
    public void remove(String map, String key) {
        hazelcastManager.remove(map, key);
    }

    public static void stopServer(){
        HazelcastManager.stop();
    }

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

        try{
            LocateRegistry.createRegistry(configuration.getPort());
        }catch(Exception e){

        }

        HazelcastServer server = new HazelcastServer();
        Naming.rebind(configuration.getURL(), server);

        //add a shut down hook.  mainly for testing / local development.
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
        public void run(){
                HazelcastManager.stop();
            }
        });
    }

}
