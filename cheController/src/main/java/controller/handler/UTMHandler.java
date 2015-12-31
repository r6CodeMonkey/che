package controller.handler;

import core.HazelcastManagerInterface;
import model.client.Location;
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

    public void handleUTMChange(UTMLocation currentLocation, UTMLocation previousLocation, TopicSubscriptions subscriptions, String uid) throws RemoteException {
        hazelcastManagerInterface.unSubscribe(previousLocation.utm.getUtm(), subscriptions);
        subscriptions.addSubscription(currentLocation.utm.getUtm(), hazelcastManagerInterface.subscribe(currentLocation.utm.getUtm(), uid));
        handleSubUTMChange(currentLocation, previousLocation, subscriptions, uid);
    }

    public void handleSubUTMChange(UTMLocation currentLocation, UTMLocation previousLocation, TopicSubscriptions subscriptions, String uid) throws RemoteException {
        hazelcastManagerInterface.unSubscribe(previousLocation.subUtm.getUtm(), subscriptions);
        subscriptions.addSubscription(currentLocation.subUtm.getUtm(), hazelcastManagerInterface.subscribe(currentLocation.subUtm.getUtm(), uid));
    }

}
