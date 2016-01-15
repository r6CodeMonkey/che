package model;

/**
 * Created by timmytime on 15/01/16.
 */
public class UTMLocation extends CoreModel {

    public UTM utm;
    public UTM subUtm;
    public double latitude, longitude, altitude, speed;

    public UTMLocation() {
        utm = new UTM();
        subUtm = new UTM();
    }

    public UTMLocation(String key) {
        super(key);
    }

    @Override
    public String getMessage() {
        return null;
    }
}
