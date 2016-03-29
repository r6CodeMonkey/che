package server;

import core.HazelcastManagerInterface;
import engine.GameEngine;
import model.GameEngineModel;
import util.Configuration;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by timmytime on 04/03/16.
 */
public class GameEngineServer extends UnicastRemoteObject implements GameEngineInterface {

    private static Configuration configuration;
    private static HazelcastManagerInterface hazelcastManagerInterface;
    private final Object lock = new Object();
    private GameEngine gameEngine = null;
    private boolean hazelcastServerUp = false;

    public GameEngineServer() throws RemoteException {
        super(configuration.getPort());
        hazelcastServerUp = initHazelcastServer();
    }

    public static void startServer() throws Exception {

        configuration = new Configuration();

        try {
            LocateRegistry.createRegistry(configuration.getPort());
        } catch (Exception e) {
            configuration.getLogger().error("create reg error " + e.getMessage());
        }


        GameEngineServer server = new GameEngineServer();
        Naming.rebind(configuration.getURL(), server);
    }

    public static void main(String[] args) throws Exception {
        startServer();
    }

    private boolean initHazelcastServer() {
        try {
            hazelcastManagerInterface = (HazelcastManagerInterface) Naming.lookup(configuration.getHazelcastURL());
            return true;
        } catch (NotBoundException e) {
            configuration.getLogger().error("hazelcast server failed " + e.getMessage());
        } catch (MalformedURLException e) {
            configuration.getLogger().error("hazelcast server failed " + e.getMessage());
        } catch (RemoteException e) {
            configuration.getLogger().error("hazelcast server failed " + e.getMessage());
        }
        return false;
    }

    @Override
    public void addGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {
        gameEngine.addGameEngineModel(gameEngineModel);
    }

    @Override
    public void removeGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {
        gameEngine.removeGameEngineModel(gameEngineModel);
    }

    private void engineStartThread() {
        new Thread(() -> {
            try {
                long startEngineTime = System.currentTimeMillis();
                configuration.getLogger().debug("game engine started");
                gameEngine.engine();
                engineStopThread(System.currentTimeMillis() - startEngineTime);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void engineStopThread(long engineRunTime) {
        new Thread(() -> {
            long time = System.currentTimeMillis();

            configuration.getLogger().debug("game engine waiting");

            while (System.currentTimeMillis() < time + (configuration.getGameEngineDelta() - engineRunTime)) {
                //do sweet fa..
            }

            engineStartThread();

        }).start();
    }

    @Override
    public void startEngine() throws RemoteException {

        configuration.getLogger().debug("called start");

        if (hazelcastManagerInterface == null) {
            hazelcastServerUp = initHazelcastServer();
        }

        gameEngine = new GameEngine(hazelcastManagerInterface, configuration);

        engineStartThread();

    }


}
