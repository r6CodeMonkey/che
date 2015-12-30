package model;

import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;

import java.io.Serializable;

/**
 * Created by timmytime on 10/12/15.
 */
public class Location extends JSONObject implements Serializable {

    private double latitude, longitude, speed, altitude;
    private String utm, subUtm;

    public Location(String location) throws JSONException {
        super(location);
        setAll();
    }

    public Location(JSONObject location) throws JSONException {
        super(location);
        setAll();
    }

    private void setAll() {
        setUtm(this.getString(Tags.UTM));
        setSubUtm(this.getString(Tags.SUB_UTM));
        setLatitude(this.getDouble(Tags.LATITUTDE));
        setLongitude(this.getDouble(Tags.LONGITUDE));
        setSpeed(this.getDouble(Tags.SPEED));
        setAltitude(this.getDouble(Tags.ALTITUDE));

    }

    public String getUtm() {
        return utm;
    }

    public void setUtm(String utm) {
        this.utm = utm;
    }

    public String getSubUtm() {
        return subUtm;
    }

    public void setSubUtm(String subUtm) {
        this.subUtm = subUtm;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
