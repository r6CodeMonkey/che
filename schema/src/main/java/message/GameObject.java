package message;

import org.json.JSONArray;
import org.json.JSONObject;
import util.Tags;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 15/01/16.
 */
public class GameObject extends CoreMessage {

    public GameObject() {
        super(Tags.GAME_OBJECT);
    }

    public GameObject(String message) {
        super(Tags.GAME_OBJECT, message);
    }


    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        inner.put(Tags.GAME_OBJECT_KEY, "");
        inner.put(Tags.GAME_OBJECT_MASS, 0);
        inner.put(Tags.GAME_OBJECT_VELOCITY, 0);
        inner.put(Tags.GAME_OBJECT_ACCELERATION, 0);
        inner.put(Tags.GAME_OBJECT_IS_FIXED, false);
        inner.put(Tags.GAME_OBJECT_IS_LOCATED, false);
        inner.put(Tags.GAME_OBJECT_IS_DESTROYED, false);
        inner.put(Tags.GAME_OBJECT_IS_HIT, false);
        inner.put(Tags.VALUE, "");
        inner.put(Tags.STATE, "");
        List<Missile> missiles = new ArrayList<>();
        inner.put(Tags.GAME_OBJECT_MISSILES, missiles);
        UTMLocation utmLocation = new UTMLocation();
        utmLocation.create();
        inner.put(Tags.GAME_OBJECT_UTM_LOCATION, utmLocation);
        this.put(Tags.GAME_OBJECT, inner);
    }


    @Override
    public String getKey() {
        return this.getJSONObject(Tags.GAME_OBJECT).get(Tags.GAME_OBJECT_KEY).toString();
    }

    @Override
    public void setKey(String key) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_KEY, key);

    }

    public String getState() {
        return this.getJSONObject(Tags.GAME_OBJECT).get(Tags.STATE).toString();
    }

    public void setState(String state) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.STATE, state);
    }

    public String getValue() {
        return this.getJSONObject(Tags.GAME_OBJECT).get(Tags.VALUE).toString();
    }

    public void setValue(String value) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.VALUE, value);
    }

    public UTMLocation getUtmLocation() {
        return new UTMLocation(this.getJSONObject(Tags.GAME_OBJECT).getJSONObject(Tags.GAME_OBJECT_UTM_LOCATION).toString());
    }

    public void setUtmLocation(UTMLocation utmLocation) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_UTM_LOCATION, utmLocation);
    }

    public double getMass() {
        return this.getJSONObject(Tags.GAME_OBJECT).getDouble(Tags.GAME_OBJECT_MASS);
    }

    public void setMass(double mass) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_MASS, mass);
    }

    public double getAcceleration() {
        return this.getJSONObject(Tags.GAME_OBJECT).getDouble(Tags.GAME_OBJECT_ACCELERATION);
    }

    public void setAcceleration(double acceleration) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_ACCELERATION, acceleration);
    }

    public double getVelocity() {
        return this.getJSONObject(Tags.GAME_OBJECT).getDouble(Tags.GAME_OBJECT_VELOCITY);
    }

    public void setVelocity(double velocity) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_VELOCITY, velocity);
    }

    public boolean isHit() {
        return this.getJSONObject(Tags.GAME_OBJECT).getBoolean(Tags.GAME_OBJECT_IS_HIT);
    }

    public void setHit(boolean hit) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_IS_HIT, hit);
    }

    public boolean isFixed() {
        return this.getJSONObject(Tags.GAME_OBJECT).getBoolean(Tags.GAME_OBJECT_IS_FIXED);
    }

    public void setFixed(boolean fixed) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_IS_FIXED, fixed);
    }

    public boolean isDestroyed() {
        return this.getJSONObject(Tags.GAME_OBJECT).getBoolean(Tags.GAME_OBJECT_IS_DESTROYED);
    }

    public void setDestroyed(boolean destroyed) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_IS_DESTROYED, destroyed);
    }

    public boolean isLocated() {
        return this.getJSONObject(Tags.GAME_OBJECT).getBoolean(Tags.GAME_OBJECT_IS_LOCATED);
    }

    public void setLocated(boolean located) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_IS_LOCATED, located);
    }

    public List<Missile> getMissiles() {
        JSONArray array = this.getJSONObject(Tags.GAME_OBJECT).getJSONArray(Tags.GAME_OBJECT_MISSILES);

        List<Missile> missiles = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            missiles.add(new Missile(array.get(i).toString()));
        }

        return missiles;
    }

    public void setMissiles(List<Missile> missiles) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_MISSILES, missiles);
    }

}
