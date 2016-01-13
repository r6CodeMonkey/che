package controller.handler;

import core.HazelcastManagerInterface;
import model.client.Location;
import model.server.Player;
import model.server.TopicSubscriptions;
import model.server.UTMLocation;
import util.Configuration;

import java.rmi.RemoteException;

/**
 * Created by timmytime on 31/12/15.
 */
public class UTMHandler {

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;

    public UTMHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
    }

    public UTMLocation getUTMLocation(Location location) {

        UTMLocation utmLocation = new UTMLocation(location);

        utmLocation.utm = configuration.getUtmConvert().getUTMGrid(utmLocation.latitude, utmLocation.longitude);
        utmLocation.subUtm = configuration.getUtmConvert().getUTMSubGrid(utmLocation.utm, utmLocation.latitude, utmLocation.longitude);

        return utmLocation;
    }

    public void handleUTMChange(UTMLocation currentLocation, Player player) throws RemoteException {
        hazelcastManagerInterface.unSubscribe(player.utmLocation.utm.getUtm(), player.getTopicSubscriptions());
        player.getTopicSubscriptions().addSubscription(currentLocation.utm.getUtm(), hazelcastManagerInterface.subscribe(currentLocation.utm.getUtm(), player.uid));
        handleSubUTMChange(currentLocation,player);
    }

    public void handleSubUTMChange(UTMLocation currentLocation, Player player) throws RemoteException {
        hazelcastManagerInterface.unSubscribe(player.utmLocation.subUtm.getUtm(), player.getTopicSubscriptions());
        player.getTopicSubscriptions().addSubscription(currentLocation.subUtm.getUtm(), hazelcastManagerInterface.subscribe(currentLocation.subUtm.getUtm(), player.uid));
    }

}
