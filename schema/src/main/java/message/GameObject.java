package message;

import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class GameObject extends CoreMessage {

    public GameObject() {
    }

    public GameObject(String message) {
        super(message);
    }


    @Override
    public void create() {

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


}
