package message;

import model.CoreModel;

/**
 * Created by timmytime on 15/01/16.
 */
public class Missile extends CoreMessage {
    public Missile(String message) {
        super(message);
    }

    public Missile(CoreModel model) {
        super(model.getMessage());
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
