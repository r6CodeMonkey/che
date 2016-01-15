package controller;

import controller.handler.GenericHandler;
import controller.handler.PlayerHandler;
import core.HazelcastManagerInterface;
import io.netty.channel.Channel;
import message.server.CheMessage;
import model.client.Core;
import model.server.Player;
import org.json.JSONException;
import server.CheCallbackInterface;
import util.Configuration;
import util.Tags;

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
    private GenericHandler genericHandler;
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
            genericHandler = new GenericHandler(hazelcastManagerInterface, configuration);
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

    public void receive(Core message) throws RemoteException, NotBoundException, MalformedURLException, JSONException, NoSuchAlgorithmException {

        if (hazelcastManagerInterface == null) {
            hazelcastServerUp = initHazelcastServer();
        }

        if (hazelcastServerUp) {
            Player player = playerHandler.handlePlayer(message);

            configuration.getLogger().debug("something gone wrong " + message.toString());

            if (!message.isNull(Tags.GENERIC_OBJECT)) {
                configuration.getLogger().debug("we have generic object");
                genericHandler.handle(player, message.getGeneric());
            }
        }
    }

    private void handleMessage(CheMessage cheMessage, String key) throws JSONException {

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
                CheMessage cheMessage = new CheMessage(message);
                handleMessage(cheMessage, key);
            } catch (JSONException e) {
                configuration.getLogger().error("callback failed " + e.getMessage());
            }
        }
    }


}
