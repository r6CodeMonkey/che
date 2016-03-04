package server;

import model.GameEngineModel;

import java.rmi.RemoteException;

/**
 * Created by timmytime on 04/03/16.
 */
public interface GameEngineInterface {

    public void addGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException;

    public void removeGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException;

    //perhaps a remote start?  who knows...should start on creation.

}
