package message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 09/12/15.
 */
public interface MessageInterface {

    public JSONObject get() throws JSONException;

    public void create(String message) throws JSONException;

}
