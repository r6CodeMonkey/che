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
        inner.put(Tags.VALUE, "");
        inner.put(Tags.STATE, "");
        UTM utm = new UTM();
        utm.create();
        inner.put(Tags.UTM, utm);

        utm = new UTM();
        utm.create();
        inner.put(Tags.SUB_UTM, utm);

        this.put(Tags.UTM_LOCATION, inner);
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

    }

    public String getValue(){
        return this.getJSONObject(Tags.UTM_LOCATION).get(Tags.VALUE).toString();
    }

    public void setValue(String value){
        this.getJSONObject(Tags.UTM_LOCATION).put(Tags.VALUE, value);
    }

    public String getState(){
        return this.getJSONObject(Tags.UTM_LOCATION).get(Tags.STATE).toString();
    }

    public void setState(String state){
        this.getJSONObject(Tags.UTM_LOCATION).put(Tags.STATE, state);
    }

    public UTM getUTM() {
        return new UTM(this.getJSONObject(Tags.UTM_LOCATION).getJSONObject(Tags.UTM).toString());
    }

    public void setUTM(UTM utm) {
        this.getJSONObject(Tags.UTM_LOCATION).put(Tags.UTM, utm);
    }

    public UTM getSubUTM() {
        return new UTM(this.getJSONObject(Tags.UTM_LOCATION).getJSONObject(Tags.SUB_UTM).toString());
    }

    public void setSubUTM(UTM utm) {
        this.getJSONObject(Tags.UTM_LOCATION).put(Tags.SUB_UTM, utm);
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
