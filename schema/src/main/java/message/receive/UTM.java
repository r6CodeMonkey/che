package message.receive;

import message.MessageInterface;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 31/12/15.
 */
public class UTM implements MessageInterface {

    private model.client.UTM utm;

    @Override
    public JSONObject get() throws JSONException {
        return utm;
    }

    @Override
    public void create(String message) throws JSONException {
        utm = new model.client.UTM(message);
    }
}
