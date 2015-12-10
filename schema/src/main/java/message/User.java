package message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 09/12/15.
 */
public class User implements MessageInterface {

    private model.User user;

    public JSONObject get() throws JSONException {
        return user;
    }

    public void create(String message) throws JSONException {
        user = new model.User(message);
    }
}
