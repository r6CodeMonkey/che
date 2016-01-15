package message;

import model.CoreModel;

/**
 * Created by timmytime on 15/01/16.
 */
public class Acknowledge extends CoreMessage {
    public Acknowledge(String message) {
        super(message);
    }

    public Acknowledge(CoreModel model) {
        super(model);
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

}
