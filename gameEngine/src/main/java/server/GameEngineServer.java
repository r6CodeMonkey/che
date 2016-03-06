package server;

import core.HazelcastManagerInterface;
import engine.GameEngine;
import model.GameEngineModel;
import util.Configuration;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by timmytime on 04/03/16.
 */
public class GameEngineServer extends UnicastRemoteObject implements GameEngineInterface{

    private GameEngine gameEngine = null;
    private Thread gameEngineThread = null;
    private static Configuration configuration;
    private boolean ENGINE_RUNNING = false;

    public GameEngineServer() throws RemoteException{
        super(0);
    }

    public static void startServer() throws Exception {
        configuration = new Configuration();

        try {
            LocateRegistry.createRegistry(configuration.getPort());
        } catch (Exception e) {

        }


        GameEngineServer server = new GameEngineServer();
        Naming.rebind(configuration.getURL(), server);

        //add a shut down hook.  mainly for testing / local development.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {

            }
        });
    }

    public static void main(String[] args) throws Exception {
        startServer();
    }

    @Override
    public void addGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {

    }

    @Override
    public void removeGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {

    }

    @Override
    public void startEngine(HazelcastManagerInterface hazelcastManagerInterface) throws RemoteException {

        gameEngine = new GameEngine(hazelcastManagerInterface, configuration);

        gameEngineThread = new Thread(() -> {
            try {
                    gameEngine.engine();
                }catch(RemoteException e){
                    e.printStackTrace();
            }
        });

        ENGINE_RUNNING = true;

        while(ENGINE_RUNNING) {
            try {
                gameEngineThread.wait(configuration.getGameEngineDelta()/2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameEngineThread.start();
        }

    }

    @Override
    public void stopEngine() throws RemoteException {
        ENGINE_RUNNING = false;
        gameEngineThread.interrupt();
    }
}
