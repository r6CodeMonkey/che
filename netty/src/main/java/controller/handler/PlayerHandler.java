package controller.handler;

import controller.CheController;
import core.HazelcastManagerInterface;
import factory.CheChannelFactory;
import message.CheMessage;
import message.HazelcastMessage;
import model.Player;
import model.UTMLocation;
import org.json.JSONException;
import org.json.JSONObject;
import util.Configuration;
import util.Tags;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

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

    public Player handlePlayer(CheMessage message) throws RemoteException, JSONException, NoSuchAlgorithmException {
        Player player = getPlayer((message.Player) message.getMessage(Tags.PLAYER));

        message.Player playerMessage = (message.Player) message.getMessage(Tags.PLAYER);
        UTMLocation utmLocation = utmHandler.getUTMLocation(playerMessage.getUTMLocation());

        boolean hasUTMChanged = player.hasUTMChanged(utmLocation);
        boolean hasSubUTMChanged = player.hasSubUTMChanged(utmLocation);


        configuration.getLogger().debug("got our utms");


        if (hasUTMChanged) {
            utmHandler.handleUTMChange(utmLocation, player);
            configuration.getLogger().debug("UTM has changed");
        } else if (hasSubUTMChanged) {
            utmHandler.handleSubUTMChange(utmLocation, player);
            configuration.getLogger().debug("Sub UTM has changed");
        }

        player.utmLocation = utmLocation;

        if (hasUTMChanged || hasSubUTMChanged) {
            CheChannelFactory.write(player.getKey(), new CheMessage(Tags.UTM_LOCATION, new message.UTMLocation(utmLocation.getMessage())));
            //we dont actually need to send this? well we could.  needs information status.
            player.utmLocation.state = Tags.MESSAGE;
            player.utmLocation.value = Tags.PLAYER_ENTERED;
            HazelcastMessage hazelcastMessage = new HazelcastMessage(CheChannelFactory.getCheChannel(player.getKey()).getChannel().remoteAddress().toString(), new JSONObject(player.utmLocation.getMessage()));
            hazelcastManagerInterface.publish(player.utmLocation.utm.getUtm()+player.utmLocation.subUtm.getUtm(), hazelcastMessage.toString());
        }

        hazelcastManagerInterface.put(Tags.PLAYER_MAP, player.getKey(), player);

        return player;
    }

    public Player getPlayer(message.Player message) throws RemoteException {

        Player player = (Player) hazelcastManagerInterface.get(Tags.PLAYER_MAP, message.getKey());

        if (player == null) {
            configuration.getLogger().debug("created a new player");
            player = new Player(message.getKey());
            player.name = message.getName();
            hazelcastManagerInterface.put(Tags.PLAYER_MAP, player.getKey(), player);
            //image to sort out.
        }

        return player;
    }

}
