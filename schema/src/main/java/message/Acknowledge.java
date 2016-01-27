package message;

import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class Acknowledge extends CoreMessage {

    private final boolean che;

    public Acknowledge() {
        super(Tags.ACKNOWLEDGE);
        che = false;
    }

    public Acknowledge(boolean che) {
        super(che ? Tags.CHE_ACKNOWLEDGE : Tags.ACKNOWLEDGE);
        this.che = che;
    }


    public Acknowledge(String message) {
        super(Tags.ACKNOWLEDGE, message);
        che = false;
    }

    public Acknowledge(boolean che, String message) {
        super(che ? Tags.CHE_ACKNOWLEDGE : Tags.ACKNOWLEDGE, message);
        this.che = che;
    }

    //a little hack for returning unlikely required for any other messages.  plus we dont want to touch java8 models, unless we can test it on client tonight
    public static CoreMessage create(String tag, String contents){
        Acknowledge acknowledge = new Acknowledge(tag.equals(Tags.CHE_ACKNOWLEDGE));
        JSONObject jsonObject = new JSONObject(contents);

        acknowledge.put(acknowledge.che ? Tags.CHE_ACK_ID : Tags.ACK_ID, jsonObject.getString(acknowledge.che ? Tags.CHE_ACK_ID : Tags.ACK_ID));
        acknowledge.put(Tags.VALUE, jsonObject.getString(Tags.VALUE));
        acknowledge.put(Tags.STATE, jsonObject.getString(Tags.STATE));

        return acknowledge;
    }

    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        inner.put(che ? Tags.CHE_ACK_ID : Tags.ACK_ID, "");
        inner.put(Tags.STATE, "");
        inner.put(Tags.VALUE, "");
        this.put(che ? Tags.CHE_ACKNOWLEDGE : Tags.ACKNOWLEDGE, inner);
    }

    @Override
    public String getKey() {
        return this.getJSONObject(che ? Tags.CHE_ACKNOWLEDGE : Tags.ACKNOWLEDGE).get(Tags.ACK_ID).toString();
    }

    @Override
    public void setKey(String key) {
        this.getJSONObject(che ? Tags.CHE_ACKNOWLEDGE : Tags.ACKNOWLEDGE).put(che ? Tags.CHE_ACK_ID : Tags.ACK_ID, key);
    }

    public String getState() {
        return this.getJSONObject(che ? Tags.CHE_ACKNOWLEDGE : Tags.ACKNOWLEDGE).get(Tags.STATE).toString();
    }

    public void setState(String state) {
        this.getJSONObject(che ? Tags.CHE_ACKNOWLEDGE : Tags.ACKNOWLEDGE).put(Tags.STATE, state);
    }

    public String getValue() {
        return this.getJSONObject(che ? Tags.CHE_ACKNOWLEDGE : Tags.ACKNOWLEDGE).get(Tags.VALUE).toString();
    }

    public void setValue(String value) {
        this.getJSONObject(che ? Tags.CHE_ACKNOWLEDGE : Tags.ACKNOWLEDGE).put(Tags.VALUE, value);
    }

    public boolean isChe(){
        return che;
    }


}
