package message;

import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class UTMLocation extends CoreMessage {

    public UTMLocation() {
    }

    public UTMLocation(String message) {
        super(message);
    }


    @Override
    public void create() {

        JSONObject inner = new JSONObject();
        inner.put(Tags.LATITUTDE, "");
        inner.put(Tags.LONGITUDE, "");
        inner.put(Tags.SPEED, "");
        inner.put(Tags.ALTITUDE, "");
        UTM utm = new UTM();
        utm.create();
        inner.put(Tags.UTM, utm);

        this.put(Tags.UTM_LOCATION, inner);
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

    }

    public UTM getUTM() {
        return new UTM(this.getJSONObject(Tags.UTM_LOCATION).getJSONObject(Tags.UTM).toString());
    }

    public void setUTM(UTM utm) {
        this.getJSONObject(Tags.UTM_LOCATION).put(Tags.UTM, utm);
    }

    public double getLatitude() {
        return this.getJSONObject(Tags.UTM_LOCATION).getDouble(Tags.LATITUTDE);
    }

    public void setLatitude(double latitude) {
        this.getJSONObject(Tags.UTM_LOCATION).put(Tags.LATITUTDE, latitude);

    }

    public double getLongitude() {
        return this.getJSONObject(Tags.UTM_LOCATION).getDouble(Tags.LONGITUDE);
    }

    public void setLongitude(double longitude) {
        this.getJSONObject(Tags.UTM_LOCATION).put(Tags.LONGITUDE, longitude);

    }

    public double getAltitude() {
        return this.getJSONObject(Tags.UTM_LOCATION).getDouble(Tags.ALTITUDE);
    }

    public void setAltitude(double altitude) {
        this.getJSONObject(Tags.UTM_LOCATION).put(Tags.ALTITUDE, altitude);

    }

    public double getSpeed() {
        return this.getJSONObject(Tags.UTM_LOCATION).getDouble(Tags.SPEED);
    }

    public void setSpeed(double speed) {
        this.getJSONObject(Tags.UTM_LOCATION).put(Tags.SPEED, speed);

    }


}
