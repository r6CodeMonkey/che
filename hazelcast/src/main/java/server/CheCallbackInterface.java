package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by timmytime on 31/12/15.
 */
public interface CheCallbackInterface extends Remote {

    void handleCallback(String message, String key) throws RemoteException;
}
