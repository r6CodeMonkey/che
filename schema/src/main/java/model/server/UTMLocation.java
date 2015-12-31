package model.server;

import model.client.Location;

import java.io.Serializable;

/**
 * Created by timmytime on 31/12/15.
 */
public class UTMLocation implements Serializable {

    public UTM utm = new UTM();
    public UTM subUtm = new UTM();

    public double latitude, longitude, altitude, speed;

    public UTMLocation() {

    }

    public UTMLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.speed = location.getSpeed();
        this.altitude = location.getAltitude();
    }


}
