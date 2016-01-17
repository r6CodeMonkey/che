package model;

/**
 * Created by timmytime on 15/01/16.
 */
public class Missile extends CoreModel {

    public String state, value;
    //this contains speed and altitude for trajectory / timing so all good....can make up the current part.
    public UTMLocation currentUTMLocation, startUTMLocation, targetUTMLocation;
    //probably need pay loads and type of strikes.   on basis you can buy missiles for game objects.
    public double impactRadius, radiusImpactScalar, payLoad, range;
    //ie is it active or destroyed.  or stastic.  ie !launched
    public boolean launched, destroyed;


    public Missile(message.Missile missile) {
        super(missile.getKey());
        this.state = missile.getState();
        this.value = missile.getValue();
        this.currentUTMLocation = new UTMLocation(missile.getCurrentUTMLocation());
        this.startUTMLocation = new UTMLocation(missile.getStartUTMLocation());
        this.targetUTMLocation = new UTMLocation(missile.getTargetUTMLocation());
        this.impactRadius = missile.getImpactRadius();
        this.radiusImpactScalar = missile.getImpactRadiusScalar();
        this.payLoad = missile.getPayLoad();
        this.range = missile.getRange();
        this.launched = missile.isLaunched();
        this.destroyed = missile.isDestroyed();

    }

    public Missile(String key) {
        super(key);
        currentUTMLocation = new UTMLocation();
        startUTMLocation = new UTMLocation();
        targetUTMLocation = new UTMLocation();
        launched = false;
        destroyed = false;
        state = "";
        value = "";
        impactRadius = 0;
        radiusImpactScalar = 0;
        payLoad = 0;
        range = 0;

    }

    @Override
    public String getMessage() {
        message.Missile missile = new message.Missile();
        missile.create();

        missile.setKey(key);
        missile.setState(state);
        missile.setValue(value);
        missile.setCurrentUTMLocation(new message.UTMLocation(currentUTMLocation.getMessage()));
        missile.setStartUTMLocation(new message.UTMLocation(startUTMLocation.getMessage()));
        missile.setTargetUTMLocation(new message.UTMLocation(targetUTMLocation.getMessage()));
        missile.setLaunched(launched);
        missile.setDestroyed(destroyed);
        missile.setPayLoad(payLoad);
        missile.setRange(range);
        missile.setImpactRadius(impactRadius);
        missile.setImpactRadiusScalar(radiusImpactScalar);

        return missile.toString();
    }
}
