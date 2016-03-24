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

    public UTMLocation getUTMLocation(message.UTMLocation location) {

        UTMLocation utmLocation = new UTMLocation(location);
        utmLocation.utm = configuration.getUtmConvert().getUTMGrid(utmLocation.latitude, utmLocation.longitude);
        utmLocation.subUtm = configuration.getUtmConvert().getUTMSubGrid(utmLocation.utm, utmLocation.latitude, utmLocation.longitude);

        return utmLocation;
    }

    public void handleUTMChange(UTMLocation currentLocation, Player player) throws RemoteException {
        //review.  we really do not want utm wide topics...
        //hazelcastManagerInterface.unSubscribe(player.utmLocation.utm.getUtm(), player.getTopicSubscriptions());
       // player.getTopicSubscriptions().addSubscription(currentLocation.utm.getUtm(), hazelcastManagerInterface.subscribe(currentLocation.utm.getUtm(), player.getKey()));
        handleSubUTMChange(currentLocation, player);
    }

    public void handleSubUTMChange(UTMLocation currentLocation, Player player) throws RemoteException {

        configuration.getLogger().debug("subscription checks fails now!");

        hazelcastManagerInterface.unSubscribe(player.utmLocation.utm.getUtm() + player.utmLocation.subUtm.getUtm(), player.getKey(), player.getKey());

        configuration.getLogger().debug("subscription checks");

        //can we actually remove it.
        if(player.getTopicSubscriptions().getSubscription(player.utmLocation.utm.getUtm() + player.utmLocation.subUtm.getUtm()) != null) {
            if (player.getTopicSubscriptions().getSubscription(player.utmLocation.utm.getUtm() + player.utmLocation.subUtm.getUtm()).containsKey(player.getKey())) {
                player.getTopicSubscriptions().removeSubscription(player.utmLocation.utm.getUtm() + player.utmLocation.subUtm.getUtm());
            }
        }


        if(player.getTopicSubscriptions().getSubscription(player.utmLocation.utm.getUtm() + player.utmLocation.subUtm.getUtm()) != null
        || !player.getTopicSubscriptions().getKeySet().contains(currentLocation.utm.getUtm()+currentLocation.subUtm.getUtm())) {
            configuration.getLogger().debug("sub utm changed so subscribing again");
            player.getTopicSubscriptions().addSubscription(currentLocation.utm.getUtm() + currentLocation.subUtm.getUtm(), player.getKey(),
                    hazelcastManagerInterface.subscribe(
                            currentLocation.utm.getUtm() + currentLocation.subUtm.getUtm(), player.getKey(), player.getKey()));
        }
    }

}
