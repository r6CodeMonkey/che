package message;

import org.json.JSONObject;

/**
 * Created by timmytime on 15/01/16.
 */
public abstract class CoreMessage extends JSONObject implements MessageInterface {


    public CoreMessage() {

    }

    public CoreMessage(String message) {
        super(message);

    }


}