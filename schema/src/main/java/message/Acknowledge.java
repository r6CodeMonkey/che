package message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 10/12/15.
 */
public class Acknowledge implements MessageInterface {

    private model.Acknowledge acknowledge;

    public JSONObject get() throws JSONException {
        return acknowledge;
    }

    public void create(String message) throws JSONException {
        acknowledge = new model.Acknowledge(message);
    }
}
