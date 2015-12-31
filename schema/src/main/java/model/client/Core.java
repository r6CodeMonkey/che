package model.client;

import model.client.generic.GenericModel;
import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;

import java.io.Serializable;

/**
 * Created by timmytime on 10/12/15.
 */
public class Core extends JSONObject implements Serializable {


    public Core(String core) throws JSONException {
        super(core);
    }

    public String getAckId() throws JSONException {
        return this.getString(Tags.ACK_ID);
    }

    public User getUser() throws JSONException {
        return new User(this.getJSONObject(Tags.USER_OBJECT));
    }

    public void updateUserID(String id) throws JSONException {
        this.getJSONObject(Tags.USER_OBJECT).put(Tags.UID, id);
    }

    public Location getLocation() throws JSONException {
        return new Location(this.getJSONObject(Tags.LOCATION_OBJECT));
    }

    public GenericModel getGeneric() throws JSONException {
        return new GenericModel(this.getJSONObject(Tags.GENERIC_OBJECT));
    }

    public Acknowledge getAcknowledge() throws JSONException {
        return (new Acknowledge(this.getJSONObject(Tags.ACKNOWLEDGE_OBJECT)));
    }


}
