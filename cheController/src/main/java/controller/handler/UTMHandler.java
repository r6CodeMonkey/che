package controller.handler;

import core.HazelcastManagerInterface;
import model.Player;
import model.UTMLocation;
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

    public UTMLocation getUTMLocation(UTMLocation location) {

        location.utm = configuration.getUtmConvert().getUTMGrid(location.latitude, location.longitude);
        location.subUtm = configuration.getUtmConvert().getUTMSubGrid(location.utm, location.latitude, location.longitude);

        return location;
    }

    public void handleUTMChange(UTMLocation currentLocation, Player player) throws RemoteException {
        hazelcastManagerInterface.unSubscribe(player.utmLocation.utm.getUtm(), player.getTopicSubscriptions());
        player.getTopicSubscriptions().addSubscription(currentLocation.utm.getUtm(), hazelcastManagerInterface.subscribe(currentLocation.utm.getUtm(), player.uid));
        handleSubUTMChange(currentLocation, player);
    }

    public void handleSubUTMChange(UTMLocation currentLocation, Player player) throws RemoteException {
        hazelcastManagerInterface.unSubscribe(player.utmLocation.subUtm.getUtm(), player.getTopicSubscriptions());
        player.getTopicSubscriptions().addSubscription(currentLocation.subUtm.getUtm(), hazelcastManagerInterface.subscribe(currentLocation.subUtm.getUtm(), player.uid));
    }

}
