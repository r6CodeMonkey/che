package server;

import model.GameEngineModel;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by timmytime on 04/03/16.
 */
public interface GameEngineInterface extends Remote {

    public void addGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException;

    public void startEngine() throws RemoteException;


    //also need to add missile actions...no callbacks required for this interface (it uses hazelcast to callback).
}
