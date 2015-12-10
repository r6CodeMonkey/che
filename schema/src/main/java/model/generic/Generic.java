package model.generic;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 10/12/15.
 */
public class Generic extends JSONObject implements GenericInterface {

    public Generic(String generic) throws JSONException {
        super(generic);
    }

    public Generic(JSONObject generic) throws JSONException {
        super(generic);
    }


    public void getId() {

    }

    public void getValue(String value) {

    }

    public void getStatus(String status) {

    }
}
