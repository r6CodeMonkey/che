package message.out;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 23/02/15.
 */
public class Message implements MessageInterface {

    protected JSONObject message = new JSONObject();

    public static final String ADDRESS = "address";

    public static final String TIME = "time";

    public static final String GRID_UTM = "utm";
    public static final String SUB_UTM = "subutm";

    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "long";

    public static final String SPEED = "speed";
    public static final String ALTITUDE = "altitude";


    public static final String KEY = "key";
    public static final String TYPE = "type";


    public static final String CORE = "core";


    public Message() throws JSONException {
        JSONObject inner = new JSONObject();
        inner.put(TIME, System.currentTimeMillis());
        message.put(CORE, inner);
    }

    @Override
    public JSONObject getCoreMessage() throws JSONException {
        return message.getJSONObject(CORE);
    }

    @Override
    public JSONObject getMessage() throws JSONException {
        return message;
    }

}
