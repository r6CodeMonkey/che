package message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by timmytime on 15/01/16.
 */
public class HazelcastMessage extends JSONObject implements Serializable {

    public static final String REMOTE_ADDRESS = "remoteAddress";
    public static final String CHE_OBJECT = "cheObject";

    public HazelcastMessage(String cheMessage) throws JSONException {
        super(cheMessage);
    }

    public HazelcastMessage(String remoteAddress, JSONObject cheObject) throws JSONException {
        this.put(REMOTE_ADDRESS, remoteAddress);
        this.put(CHE_OBJECT, cheObject);
    }

    public String getRemoteAddress() throws JSONException {
        return this.getString(REMOTE_ADDRESS);
    }

    public JSONObject getCheObject() throws JSONException {
        return this.getJSONObject(CHE_OBJECT);
    }
}
