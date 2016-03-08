package controller;

import controller.handler.CheHandler;
import controller.handler.PlayerHandler;
import core.HazelcastManagerInterface;
import factory.CheChannelFactory;
import io.netty.channel.Channel;
import message.CheMessage;
import model.Player;
import org.json.JSONException;
import server.GameEngineInterface;
import util.CheCallbackClient;
import util.Configuration;
import util.Tags;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by timmytime on 30/12/15.
 */
public class CheController {

    public static final String PLAYER_MAP = "PLAYER_MAP";
    public static final String ALLIANCE_MAP = "ALLIANCE_MAP";
    public static final String OBJECT_MAP = "OBJECT_MAP";
    public static final String MISSILE_MAP = "MISSILE_MAP";

    //server
    private static HazelcastManagerInterface hazelcastManagerInterface;
    private static GameEngineInterface gameEngineInterface;
    //utils
    private final Configuration configuration;
    //handlers
    private PlayerHandler playerHandler;
    private CheHandler cheHandler;
    //server up flag
    private boolean hazelcastServerUp = false;
    private boolean gameEngineServerUp = false;


    public CheController(Configuration configuration) throws Exception {
        this.configuration = configuration;
        hazelcastServerUp = initHazelcastServer();
        gameEngineServerUp = initGameEngineServer();
        //we need this on a restart...
        cheHandler = new CheHandler(hazelcastManagerInterface, gameEngineInterface, configuration);

    }

    private boolean initGameEngineServer() { //i really need to run this in a seperate instance (ie not on here).  so next up we (i) need to look at AWS...
        //time to start using that i think.  and deploy to it.  for testing lets add the engine into the source as before (not ideal mind).
        try {
            gameEngineInterface = (GameEngineInterface) Naming.lookup(configuration.getEngineURL());
            gameEngineInterface.startEngine();
            return true;
        } catch (NotBoundException e) {
            e.printStackTrace();
            configuration.getLogger().error("game engine server failed " + e.getMessage());
        } catch (MalformedURLException e) {
            configuration.getLogger().error("game engine server failed " + e.getMessage());
        } catch (RemoteException e) {
            configuration.getLogger().error("game engine server failed " + e.getMessage());
        }
        return false;
    }


    private boolean initHazelcastServer() {
        try {
            hazelcastManagerInterface = (HazelcastManagerInterface) Naming.lookup(configuration.getHazelcastURL());
            playerHandler = new PlayerHandler(hazelcastManagerInterface, configuration);
            hazelcastManagerInterface.addCallback(new CheCallbackClient(configuration));
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

    public void receive(Channel channel, CheMessage message) throws RemoteException, NotBoundException, MalformedURLException, JSONException, NoSuchAlgorithmException, JAXBException {

        if (hazelcastManagerInterface == null && gameEngineInterface == null) {
            hazelcastServerUp = initHazelcastServer();
            gameEngineServerUp = initGameEngineServer();
            cheHandler = new CheHandler(hazelcastManagerInterface, gameEngineInterface, configuration);
        }else if (hazelcastManagerInterface == null) {
            hazelcastServerUp = initHazelcastServer();
            cheHandler = new CheHandler(hazelcastManagerInterface, gameEngineInterface, configuration);
        }else if (gameEngineInterface == null) {
            gameEngineServerUp = initGameEngineServer();
            cheHandler = new CheHandler(hazelcastManagerInterface, gameEngineInterface, configuration);
        }

        if (hazelcastServerUp) {

            //do the needful ;) trademarked.  needs testing under force ie should be thread safe as static, and we never access same shit form multiple threads.
            CheChannelFactory.updateCheChannel(message.getMessage(Tags.PLAYER).getKey(), channel);

            Player player = playerHandler.handlePlayer(message);
            cheHandler.handle(player, message);

            //need to put updated player back.  also check its been updated as not re assigned etc...probably ok.
            hazelcastManagerInterface.put(CheController.PLAYER_MAP, player.getKey(), player);
        }
    }

}
