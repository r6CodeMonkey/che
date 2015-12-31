package message.send;

import message.MessageInterface;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 09/12/15.
 */
public class Location implements MessageInterface {

    private model.client.Location location;

    public JSONObject get() throws JSONException {
        return location;
    }

    public void create(String message) throws JSONException {
        location = new model.client.Location(message);
    }
}
