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
        inner.put(Tags.GAME_OBJECT_STRENGTH, 0);
        inner.put(Tags.GAME_OBJECT_FORCE, 0);
        inner.put(Tags.MISSILE_RANGE, 0);
        inner.put(Tags.MISSILE_IMPACT_RADIUS, 0);
        inner.put(Tags.GAME_OBJECT_MAX_SPEED, 0);
        inner.put(Tags.GAME_OBJECT_IS_FIXED, false);
        inner.put(Tags.GAME_OBJECT_IS_LOCATED, false);
        inner.put(Tags.GAME_OBJECT_IS_DESTROYED, false);
        inner.put(Tags.GAME_OBJECT_IS_HIT, false);
        inner.put(Tags.VALUE, "");
        inner.put(Tags.STATE, "");
        inner.put(Tags.GAME_OBJECT_TYPE, -1);
        inner.put(Tags.GAME_OBJECT_SUBTYPE, -1);
        inner.put(Tags.GAME_OBJECT_QUANTITY, 1);
        List<Missile> missiles = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(missiles);
        inner.put(Tags.GAME_OBJECT_MISSILES, jsonArray);
        List<UTM> utms = new ArrayList<>();
        jsonArray = new JSONArray(utms);
        inner.put(Tags.GAME_OBJECT_DEST_VALIDATOR, jsonArray);
        UTMLocation utmLocation = new UTMLocation();
        utmLocation.create();
        inner.put(Tags.GAME_OBJECT_UTM_LOCATION, utmLocation);
        inner.put(Tags.GAME_OBJECT_DEST_UTM_LOCATION, utmLocation);
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

    public UTMLocation getDestinationUtmLocation() {
        return new UTMLocation(this.getJSONObject(Tags.GAME_OBJECT).getJSONObject(Tags.GAME_OBJECT_DEST_UTM_LOCATION).toString());
    }

    public void setUtmLocation(UTMLocation utmLocation) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_UTM_LOCATION, utmLocation);
    }

    public void setDestinationUtmLocation(UTMLocation utmLocation) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_DEST_UTM_LOCATION, utmLocation);
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

    public int getQuantity() {
        return this.getJSONObject(Tags.GAME_OBJECT).getInt(Tags.GAME_OBJECT_QUANTITY);
    }

    public void setQuantity(int quantity) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_QUANTITY, quantity);
    }

    public int getType() {
        return this.getJSONObject(Tags.GAME_OBJECT).getInt(Tags.GAME_OBJECT_TYPE);
    }

    public void setType(int type) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_TYPE, type);
    }

    public int getSubType() {
        return this.getJSONObject(Tags.GAME_OBJECT).getInt(Tags.GAME_OBJECT_SUBTYPE);
    }

    public void setSubType(int type) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_SUBTYPE, type);
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

    public double getImpactRadius() {
        return this.getJSONObject(Tags.GAME_OBJECT).getDouble(Tags.MISSILE_IMPACT_RADIUS);
    }

    public void setImpactRadius(double scalar) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.MISSILE_IMPACT_RADIUS, scalar);
    }

    public double getStrength() {
        return this.getJSONObject(Tags.GAME_OBJECT).getDouble(Tags.GAME_OBJECT_STRENGTH);
    }

    public void setStrength(double strength) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_STRENGTH, strength);
    }

    public double getRange() {
        return this.getJSONObject(Tags.GAME_OBJECT).getDouble(Tags.MISSILE_RANGE);
    }

    public void setRange(double range) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.MISSILE_RANGE, range);
    }

    public double getForce(){
        return this.getJSONObject(Tags.GAME_OBJECT).getDouble(Tags.GAME_OBJECT_FORCE);
    }

    public void setForce(double force){
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_FORCE, force);
    }

    public double getMaxSpeed(){
        return this.getJSONObject(Tags.GAME_OBJECT).getDouble(Tags.GAME_OBJECT_MAX_SPEED);
    }

    public void setMaxSpeed(double force){
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_MAX_SPEED, force);
    }

    public List<Missile> getMissiles() {
        JSONArray array = this.getJSONObject(Tags.GAME_OBJECT).getJSONArray(Tags.GAME_OBJECT_MISSILES);

        List<Missile> missiles = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            missiles.add(new Missile(array.get(i).toString()));
        }

        return missiles;
    }

    public List<UTM> getDestinationValidator() {
        JSONArray array = this.getJSONObject(Tags.GAME_OBJECT).getJSONArray(Tags.GAME_OBJECT_DEST_VALIDATOR);

        List<UTM> utms = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            utms.add(new UTM(array.get(i).toString()));
        }

        return utms;
    }

    public void setMissiles(List<Missile> missiles) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_MISSILES, new JSONArray(missiles));
    }

    public void setDestinationValidator(List<UTM> utms) {
        this.getJSONObject(Tags.GAME_OBJECT).put(Tags.GAME_OBJECT_DEST_VALIDATOR, new JSONArray(utms));
    }

}
