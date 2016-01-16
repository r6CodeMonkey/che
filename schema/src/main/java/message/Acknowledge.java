package message;

import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class Acknowledge extends CoreMessage {

    public Acknowledge() {

    }

    public Acknowledge(String message) {
        super(message);
    }

    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        inner.put(Tags.ACK_ID, "");
        inner.put(Tags.STATE, "");
        inner.put(Tags.VALUE, "");
        this.put(Tags.ACKNOWLEDGE, inner);
    }

    @Override
    public String getKey() {
        return this.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.ACK_ID).toString();
    }

    @Override
    public void setKey(String key) {
        this.getJSONObject(Tags.ACKNOWLEDGE).put(Tags.ACK_ID, key);
    }

    public String getState() {
        return this.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.STATE).toString();
    }

    public void setState(String state) {
        this.getJSONObject(Tags.ACKNOWLEDGE).put(Tags.STATE, state);

    }

    public String getValue() {
        return this.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.VALUE).toString();
    }

    public void setValue(String value) {
        this.getJSONObject(Tags.ACKNOWLEDGE).put(Tags.VALUE, value);
    }


}
