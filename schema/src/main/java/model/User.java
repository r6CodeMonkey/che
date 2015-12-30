package model;

import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;

import java.io.Serializable;

/**
 * Created by timmytime on 10/12/15.
 */
public class User extends JSONObject implements Serializable {


    public User(String user) throws JSONException {
        super(user);
        setUid(this.getString(Tags.UID));
    }

    public User(JSONObject user) throws JSONException {
        super(user.toString());
        setUid(this.getString(Tags.UID));
    }

    public String getUid() {
        return this.getString(Tags.UID);
    }

    public void setUid(String uid) {
        this.put(Tags.UID, uid);
    }
}
