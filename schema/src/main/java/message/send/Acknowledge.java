package message.send;

import message.MessageInterface;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 10/12/15.
 */
public class Acknowledge implements MessageInterface {

    private model.client.Acknowledge acknowledge;

    public JSONObject get() throws JSONException {
        return acknowledge;
    }

    public void create(String message) throws JSONException {
        acknowledge = new model.client.Acknowledge(message);
    }
}
