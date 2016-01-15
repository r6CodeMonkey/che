package message;

import model.CoreModel;
import org.json.JSONObject;

/**
 * Created by timmytime on 15/01/16.
 */
public abstract class CoreMessage extends JSONObject implements MessageInterface {


    private String key, type, state, value;
    private CoreMessage coreMessage;

    public CoreMessage(String message) {
        super(message);

    }

    public CoreMessage(CoreModel model) {
        super(model.getMessage());
    }

    public String getType() {
        return type;
    }

}
