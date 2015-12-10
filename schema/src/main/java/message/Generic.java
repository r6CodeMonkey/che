package message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 09/12/15.
 */
public class Generic implements MessageInterface {

    private model.generic.Generic generic;

    public JSONObject get() throws JSONException {
        return generic;
    }

    public void create(String message) throws JSONException {
        generic = new model.generic.Generic(message);
    }
}
