package model.client.generic;

import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 10/12/15.
 */
public class GenericModel extends JSONObject implements GenericInterface {


    public GenericModel(String generic) throws JSONException {
        super(generic);
    }

    public GenericModel(JSONObject generic) throws JSONException {
        super(generic.toString());
    }


    public String getType() {
        return this.getString(Tags.TYPE);
    }

    public String getId() {
        return this.getString(Tags.UID);

    }

    public String getValue() {
        return this.getString(Tags.VALUE);
    }

    public String getState() {
        return this.getString(Tags.STATE);
    }
}
