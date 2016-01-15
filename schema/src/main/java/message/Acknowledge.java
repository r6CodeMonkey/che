package message;

import model.CoreModel;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class Acknowledge extends CoreMessage {
    public Acknowledge(String message) {
        super(message);
    }

    public Acknowledge(CoreModel model) {
        super(model);
    }

    @Override
    public String getKey() {
        return this.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.ACK_ID).toString();
    }

    @Override
    public void setKey(String key) {

    }

    @Override
    public String getState() {
        return this.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.STATE).toString();
    }

    @Override
    public String getValue() {
        return this.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.VALUE).toString();
    }

}
