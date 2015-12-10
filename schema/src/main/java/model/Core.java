package model;

import model.generic.Generic;
import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 10/12/15.
 */
public class Core extends JSONObject {


    public Core(String core) throws JSONException {
        super(core);
    }

    public String getUid() throws JSONException {
        return this.getString(Tags.UID);
    }

    public User getUser() throws JSONException {
        return new User(this.getJSONObject(Tags.USER_OBJECT));
    }

    public Location getLocation() throws JSONException {
        return new Location(this.getJSONObject(Tags.LOCATION_OBJECT));
    }

    public Generic getGeneric() throws JSONException {
        return new Generic(this.getJSONObject(Tags.GENERIC_OBJECT));
    }

    public Acknowledge getAcknowledge() throws JSONException {
        return (new Acknowledge(this.getJSONObject(Tags.ACKNOWLEDGE_OBJECT)));
    }

}
