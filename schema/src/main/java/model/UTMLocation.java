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

    public UTMLocation(message.UTMLocation utmLocation) {
        //construct from message.
        utm = new UTM(utmLocation.getUTM());
        subUtm = new UTM(utmLocation.getSubUTM());
        latitude = utmLocation.getLatitude();
        longitude = utmLocation.getLongitude();
        altitude = utmLocation.getAltitude();
        speed = utmLocation.getSpeed();
    }

    public UTMLocation(String key) {
        super(key);
    }

    @Override
    public String getMessage() {

        message.UTMLocation utmLocation = new message.UTMLocation();
        utmLocation.create();

        utmLocation.setAltitude(altitude);
        utmLocation.setSpeed(speed);
        utmLocation.setLongitude(longitude);
        utmLocation.setLatitude(latitude);

        message.UTM temp = new message.UTM();
        temp.create();

        temp.setUTMLatGrid(utm.getUtmLat());
        temp.setUTMLongGrid(utm.getUtmLong());

        utmLocation.setUTM(temp);

        temp = new message.UTM();
        temp.create();

        temp.setUTMLatGrid(subUtm.getUtmLat());
        temp.setUTMLongGrid(subUtm.getUtmLong());

        utmLocation.setSubUTM(temp);

        return utmLocation.toString();
    }
}
