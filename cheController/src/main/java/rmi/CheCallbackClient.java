package rmi;

import com.hazelcast.core.Message;
import server.CheCallbackInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by timmytime on 31/12/15.
 */
public class CheCallbackClient extends UnicastRemoteObject implements CheCallbackInterface {


    public CheCallbackClient() throws RemoteException{
        super();
    }

    @Override
    public void handleCallback(Message message, String key) {

    }
}
