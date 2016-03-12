package util;

import factory.CheChannelFactory;
import message.HazelcastMessage;
import server.CheCallbackInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by timmytime on 25/01/16.
 */
public class CheCallbackClient extends UnicastRemoteObject implements CheCallbackInterface {

    private final Configuration configuration;

    public CheCallbackClient(Configuration configuration) throws RemoteException {
        super();
        this.configuration = configuration;
    }

    @Override
    public void handleCallback(String message, String key) {
        try {

            configuration.getLogger().debug("call back "+message+" key is "+key);

            CheChannelFactory.write(key, new HazelcastMessage(message));
        } catch (Exception e) {
            configuration.getLogger().error("callback failed " + message + " " + e.getMessage());

        }
    }
}

