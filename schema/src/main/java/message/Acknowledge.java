package message;

import model.CoreModel;
import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class Acknowledge extends CoreMessage {

    public Acknowledge(){

    }

    @Override
    public void create(){
        JSONObject inner = new JSONObject();
        inner.put(Tags.ACK_ID, "");
        inner.put(Tags.STATE, "");
        inner.put(Tags.VALUE, "");
        this.put(Tags.ACKNOWLEDGE, inner);
    }

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
          this.getJSONObject(Tags.ACKNOWLEDGE).put(Tags.ACK_ID, key);
    }

    public void setState(String state){
        this.getJSONObject(Tags.ACKNOWLEDGE).put(Tags.STATE, state);

    }

    public void setValue(String value){
        this.getJSONObject(Tags.ACKNOWLEDGE).put(Tags.VALUE, value);
    }

    public String getState() {
        return this.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.STATE).toString();
    }

    public String getValue() {
        return this.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.VALUE).toString();
    }


}
