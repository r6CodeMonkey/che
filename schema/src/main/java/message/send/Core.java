package message.send;

import message.MessageInterface;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 09/12/15.
 */
public class Core implements MessageInterface {

    private model.client.Core coreModel;

    public JSONObject get() throws JSONException {
        return coreModel;
    }

    public void create(String message) throws JSONException {
        coreModel = new model.client.Core(message);
    }
}
