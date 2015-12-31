package message.send;

import message.MessageInterface;
import model.client.generic.GenericModel;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 09/12/15.
 * <p/>
 * Any specific message
 */
public class GenericMessage implements MessageInterface {

    private GenericModel genericModel;

    public JSONObject get() throws JSONException {
        return genericModel;
    }

    public void create(String message) throws JSONException {
        genericModel = new GenericModel(message);
    }
}
