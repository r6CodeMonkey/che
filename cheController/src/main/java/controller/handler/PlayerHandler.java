package controller.handler;

import controller.CheController;
import core.HazelcastManagerInterface;
import message.receive.CheMessage;
import model.client.Core;
import model.client.UTM;
import model.server.Player;
import model.server.UTMLocation;
import org.json.JSONException;
import util.Configuration;
import util.Response;

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

    public Player handlePlayer(Core message) throws RemoteException, JSONException {
        Player player = getPlayer(message.getUser().getUid());
        UTMLocation utmLocation = utmHandler.getUTMLocation(message.getLocation());

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
            UTM model = new UTM(player.utmLocation.utm.getUtm(), player.utmLocation.subUtm.getUtm());
            configuration.getChannelMapController().getChannel(player.uid).writeAndFlush(model.toString());
            //need to use the proper message type, but it works....ie publishes properly to correct listeners.
            hazelcastManagerInterface.publish(player.utmLocation.subUtm.getUtm(), "{" + CheMessage.REMOTE_ADDRESS +Response.FAKE_TAG+"," + CheMessage.CHE_OBJECT + Response.PLAYER_JOINED+"}");
        }

        hazelcastManagerInterface.put(CheController.PLAYER_MAP, player.uid, player);

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
