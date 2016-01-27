package controller;

import controller.handler.CheHandler;
import controller.handler.PlayerHandler;
import core.HazelcastManagerInterface;
import factory.CheChannelFactory;
import io.netty.channel.Channel;
import message.CheMessage;
import model.Player;
import org.json.JSONException;
import util.CheCallbackClient;
import util.Configuration;
import util.Tags;

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

    public void receive(Channel channel, CheMessage message) throws RemoteException, NotBoundException, MalformedURLException, JSONException, NoSuchAlgorithmException {

        if (hazelcastManagerInterface == null) {
            hazelcastServerUp = initHazelcastServer();
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
