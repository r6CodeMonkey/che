package server;

import engine.GameEngine;
import model.GameEngineModel;
import util.Configuration;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by timmytime on 04/03/16.
 */
public class GameEngineServer extends UnicastRemoteObject implements GameEngineInterface{

    private final GameEngine gameEngine = null;

    public GameEngineServer() throws RemoteException{
        super(0);
    }

    public static void startServer() throws Exception {
        Configuration configuration = new Configuration();

        try {
            LocateRegistry.createRegistry(configuration.getPort());
        } catch (Exception e) {

        }

       /* HazelcastServer server = new HazelcastServer();
        Naming.rebind(configuration.getURL(), server);

        //add a shut down hook.  mainly for testing / local development.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                HazelcastManager.stop();
            }
        }); */
    }

    @Override
    public void addGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {

    }

    @Override
    public void removeGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {

    }
}
