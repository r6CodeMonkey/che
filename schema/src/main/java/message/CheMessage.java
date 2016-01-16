package message;

import factory.MessageFactory;
import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class CheMessage extends CoreMessage {

    public CheMessage() {
    }

    public CheMessage(String message) {
        super(message);
    }


    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        this.put(Tags.CHE, inner);
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

    }


    public CoreMessage getMessage(String type) {
        return MessageFactory.getCheMessage(this.getJSONObject(Tags.CHE).getJSONObject(type).toString(), type);
    }

    public void setMessage(String type, CoreMessage message) {
        this.getJSONObject(Tags.CHE).put(type, message);
    }

}
