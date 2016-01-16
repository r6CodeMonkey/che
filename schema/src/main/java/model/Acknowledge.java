package model;


import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class Acknowledge extends CoreModel {

    public String state;
    public String value;

    public Acknowledge(String key) {
        super(key);
    }


    @Override
    public String getMessage() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(Tags.STATE, state);
        jsonObject.put(Tags.VALUE, value);

        return jsonObject.toString();
    }
}
