package message;

import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class Missile extends CoreMessage {

    public Missile() {
    }

    public Missile(String message) {
        super(message);
    }


    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        inner.put(Tags.MISSILE_KEY, "");
        inner.put(Tags.STATE, "");
        inner.put(Tags.VALUE, "");
        inner.put(Tags.MISSILE_RANGE, 0);
        inner.put(Tags.MISSILE_RADIUS, 0);
        inner.put(Tags.MISSILE_RADIUS_IMPACT_SCALAR, 0);
        inner.put(Tags.MISSILE_PAYLOAD, 0);
        inner.put(Tags.MISSILE_DESTROYED, false);
        inner.put(Tags.MISSILE_LAUNCHED, false);
        UTMLocation utmLocation = new UTMLocation();
        utmLocation.create();
        inner.put(Tags.MISSILE_UTM_LOCATION, utmLocation);
        inner.put(Tags.MISSILE_TARGET_UTM_LOCATION, utmLocation);
        inner.put(Tags.MISSILE_START_UTM_LOCATION, utmLocation);

        this.put(Tags.MISSILE, inner);
    }

    @Override
    public String getKey() {
        return this.getJSONObject(Tags.MISSILE).get(Tags.MISSILE_KEY).toString();
    }

    @Override
    public void setKey(String key) {
        this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_KEY, key);

    }

    public String getState(){
        return this.getJSONObject(Tags.MISSILE).get(Tags.STATE).toString();
    }

    public String getValue(){
        return this.getJSONObject(Tags.MISSILE).get(Tags.VALUE).toString();
    }

    public boolean isLaunched(){
        return this.getJSONObject(Tags.MISSILE).getBoolean(Tags.MISSILE_LAUNCHED);
    }

    public boolean isDestroyed(){
        return this.getJSONObject(Tags.MISSILE).getBoolean(Tags.MISSILE_DESTROYED);
    }

    public UTMLocation getCurrentUTMLocation(){
        return new UTMLocation(this.getJSONObject(Tags.MISSILE).getJSONObject(Tags.MISSILE_UTM_LOCATION).toString());
    }

    public UTMLocation getTargetUTMLocation(){
        return new UTMLocation(this.getJSONObject(Tags.MISSILE).getJSONObject(Tags.MISSILE_TARGET_UTM_LOCATION).toString());
    }

    public UTMLocation getStartUTMLocation(){
        return new UTMLocation(this.getJSONObject(Tags.MISSILE).getJSONObject(Tags.MISSILE_START_UTM_LOCATION).toString());
    }

    public double getImpactRadius(){
        return this.getJSONObject(Tags.MISSILE).getDouble(Tags.MISSILE_RADIUS);
    }

    public double getImpactRadiusScalar(){
        return this.getJSONObject(Tags.MISSILE).getDouble(Tags.MISSILE_RADIUS_IMPACT_SCALAR);
    }

    public double getPayLoad(){
        return this.getJSONObject(Tags.MISSILE).getDouble(Tags.MISSILE_PAYLOAD);
    }

    public double getRange(){
        return this.getJSONObject(Tags.MISSILE).getDouble(Tags.MISSILE_RANGE);
    }


    public void setState(String state){
         this.getJSONObject(Tags.MISSILE).put(Tags.STATE, state);
    }

    public void setValue(String value){
         this.getJSONObject(Tags.MISSILE).put(Tags.VALUE, value);
    }

    public void setLaunched(boolean launched){
         this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_LAUNCHED, launched);
    }

    public void setDestroyed(boolean destroyed){
         this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_DESTROYED, destroyed);
    }

    public void setCurrentUTMLocation(UTMLocation utmLocation){
          this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_UTM_LOCATION, utmLocation);
    }

    public void setTargetUTMLocation(UTMLocation utmLocation){
          this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_TARGET_UTM_LOCATION, utmLocation);
    }

    public void setStartUTMLocation(UTMLocation utmLocation){
          this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_START_UTM_LOCATION, utmLocation);
    }

    public void setImpactRadius(double radius){
         this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_RADIUS, radius);
    }

    public void setImpactRadiusScalar(double scalar){
         this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_RADIUS_IMPACT_SCALAR, scalar);
    }

    public void setPayLoad(double payLoad){
         this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_PAYLOAD, payLoad);
    }

    public void setRange(double range){
         this.getJSONObject(Tags.MISSILE).put(Tags.MISSILE_RANGE, range);
    }




}
