package message;

import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;

import java.io.Serializable;

/**
 * Created by timmytime on 15/01/16.
 */
public class HazelcastMessage extends JSONObject implements Serializable {

    public static final String REMOTE_ADDRESS = "remoteAddress";
    public static final String SEND_TO_SELF = "sendToSelf";

    public HazelcastMessage(String cheMessage) throws JSONException {
        super(cheMessage);
    }

    public HazelcastMessage(String remoteAddress, JSONObject cheObject) throws JSONException {
        this.put(Tags.HAZELCAST, new JSONObject());
        this.getJSONObject(Tags.HAZELCAST).put(REMOTE_ADDRESS, remoteAddress);
        this.getJSONObject(Tags.HAZELCAST).put(Tags.CHE, cheObject);
        this.getJSONObject(Tags.HAZELCAST).put(SEND_TO_SELF, false);
    }

    public HazelcastMessage(String remoteAddress, boolean sendToSelf, JSONObject cheObject) throws JSONException {
        this.put(Tags.HAZELCAST, new JSONObject());
        this.getJSONObject(Tags.HAZELCAST).put(REMOTE_ADDRESS, remoteAddress);
        this.getJSONObject(Tags.HAZELCAST).put(Tags.CHE, cheObject);
        this.getJSONObject(Tags.HAZELCAST).put(SEND_TO_SELF, sendToSelf);
    }

    public String getRemoteAddress() throws JSONException {
        return this.getJSONObject(Tags.HAZELCAST).get(REMOTE_ADDRESS).toString();
    }

    public JSONObject getCheObject() throws JSONException {
        return this.getJSONObject(Tags.HAZELCAST).getJSONObject(Tags.CHE);
    }

    public boolean isSendToSelf() throws JSONException {
        return this.getJSONObject(Tags.HAZELCAST).getBoolean(SEND_TO_SELF);
    }
}
