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

        message.UTMLocation utmLocation = new message.UTMLocation();
        utmLocation.create();

        utmLocation.setAltitude(altitude);
        utmLocation.setSpeed(speed);
        utmLocation.setLongitude(longitude);
        utmLocation.setLatitude(latitude);

        message.UTM temp = new message.UTM();
        temp.create();

        temp.setUTMGrid(utm.getUtm());
        temp.setSubUTMGrid(subUtm.getUtm());

        utmLocation.setUTM(temp);

        return utmLocation.toString();
    }
}
