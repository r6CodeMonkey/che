package message;

import model.CoreModel;

/**
 * Created by timmytime on 15/01/16.
 */
public class Player extends CoreMessage {
    public Player(String message) {
        super(message);
    }

    public Player(CoreModel model) {
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
