package controller;

import controller.handler.CheHandler;
import controller.handler.PlayerHandler;
import core.HazelcastManagerInterface;
import io.netty.channel.Channel;
import message.CheMessage;
import message.HazelcastMessage;
import model.Player;
import org.json.JSONException;
import server.CheCallbackInterface;
import util.Configuration;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
    //utils
    private final Configuration configuration;
    //handlers
    private PlayerHandler playerHandler;
    private CheHandler cheHandler;
    //server up flag
    private boolean hazelcastServerUp = false;

    public CheController(Configuration configuration) throws Exception {
        this.configuration = configuration;
        hazelcastServerUp = initHazelcastServer();
    }


    private boolean initHazelcastServer() {
        try {
            hazelcastManagerInterface = (HazelcastManagerInterface) Naming.lookup(configuration.getHazelcastURL());
            playerHandler = new PlayerHandler(hazelcastManagerInterface, configuration);
            cheHandler = new CheHandler(hazelcastManagerInterface, configuration);
            hazelcastManagerInterface.addCallback(new CheCallbackClient());
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

    public void receive(CheMessage message) throws RemoteException, NotBoundException, MalformedURLException, JSONException, NoSuchAlgorithmException {

        if (hazelcastManagerInterface == null) {
            hazelcastServerUp = initHazelcastServer();
        }

        if (hazelcastServerUp) {
            Player player = playerHandler.handlePlayer(message);
            cheHandler.handle(player, message);
        }
    }

    private void handleMessage(HazelcastMessage cheMessage, String key) throws JSONException {

        Channel channel = configuration.getChannelMapController().getChannel(key);

        if (!cheMessage.getRemoteAddress().equals(channel.remoteAddress().toString())) {
            channel.writeAndFlush(cheMessage.getCheObject().toString());
        }
    }


    /*
      callback handler requires local access. (working around various issues with decoupling).
     */
    public class CheCallbackClient extends UnicastRemoteObject implements CheCallbackInterface {

        public CheCallbackClient() throws RemoteException {
            super();
        }

        @Override
        public void handleCallback(String message, String key) {
            try {
                HazelcastMessage cheMessage = new HazelcastMessage(message);
                handleMessage(cheMessage, key);
            } catch (JSONException e) {
                configuration.getLogger().error("callback failed " + e.getMessage());
            }
        }
    }


}
