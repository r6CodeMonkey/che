package model;

/**
 * Created by timmytime on 15/01/16.
 */
public class Missile extends CoreModel {

    public String state, value;
    //this contains speed and altitude for trajectory / timing so all good....can make up the current part.
    public UTMLocation currentUTMLocation, startUTMLocation, targetUTMLocation;
    //probably need pay loads and type of strikes.   on basis you can buy missiles for game objects.
    public double impactRadius, radiusImpactScalar, payLoad;
    //ie is it active or destroyed.  or stastic.  ie !launched
    public boolean launched, destroyed;

    public Missile(message.Missile missile){

    }

    public Missile(String key) {
        super(key);
    }

    @Override
    public String getMessage() {
        message.Missile missile = new message.Missile();
        missile.create();

        return missile.toString();
    }
}
