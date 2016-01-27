package message;

import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public abstract class CoreMessage extends JSONObject implements MessageInterface {


    protected final String type;

    public CoreMessage() {
        type = Tags.CHE;
    }

    public CoreMessage(String type) {
        this.type = type;
    }

    public CoreMessage(String type, String message) {
        super(message);
        this.type = type;
    }

    public JSONObject getContents() {
        return this.getJSONObject(type);
    }


}
