package controller.handler;

import controller.CheController;
import core.HazelcastManagerInterface;
import factory.MessageFactory;
import message.CheMessage;
import message.HazelcastMessage;
import model.Player;
import model.UTM;
import model.UTMLocation;
import org.json.JSONException;
import util.Configuration;
import util.Response;
import util.Tags;

import java.rmi.RemoteException;

/**
 * Created by timmytime on 31/12/15.
 */
public class PlayerHandler {

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;
    private final UTMHandler utmHandler;

    public PlayerHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
        this.utmHandler = new UTMHandler(hazelcastManagerInterface, configuration);
    }

    public Player handlePlayer(CheMessage message) throws RemoteException, JSONException {
        Player player = getPlayer(message.getMessage(Tags.PLAYER).getKey());
        UTMLocation utmLocation = utmHandler.getUTMLocation(new UTMLocation((message.UTMLocation)message.getMessage(Tags.UTM_LOCATION)));

        boolean hasUTMChanged = player.hasUTMChanged(utmLocation);
        boolean hasSubUTMChanged = player.hasSubUTMChanged(utmLocation);

        if (hasUTMChanged) {
            utmHandler.handleUTMChange(utmLocation, player);
            configuration.getLogger().debug("UTM has changed");
        } else if (hasSubUTMChanged) {
            utmHandler.handleSubUTMChange(utmLocation, player);
            configuration.getLogger().debug("Sub UTM has changed");
        }

        player.utmLocation = utmLocation;

        if (hasUTMChanged || hasSubUTMChanged) {
            configuration.getChannelMapController().getChannel(player.getKey()).writeAndFlush(utmLocation.getMessage());
            //need to use the proper message type, but it works....ie publishes properly to correct listeners.
            hazelcastManagerInterface.publish(player.utmLocation.subUtm.getUtm(), "{" + HazelcastMessage.REMOTE_ADDRESS + Response.FAKE_TAG + "," + HazelcastMessage.CHE_OBJECT + Response.PLAYER_JOINED + "}");
        }

        hazelcastManagerInterface.put(CheController.PLAYER_MAP, player.getKey(), player);

        return player;
    }

    public Player getPlayer(String uid) throws RemoteException {

        Player player = (Player) hazelcastManagerInterface.get(CheController.PLAYER_MAP, uid);

        if (player == null) {
            configuration.getLogger().debug("created a new player");
            player = new Player(uid);
        }

        return player;
    }

}
