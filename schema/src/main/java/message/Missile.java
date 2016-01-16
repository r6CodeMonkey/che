package message;

import model.CoreModel;

/**
 * Created by timmytime on 15/01/16.
 */
public class Missile extends CoreMessage {

    public Missile(){}

    public Missile(String message) {
        super(message);
    }

    public Missile(CoreModel model) {
        super(model.getMessage());
    }

    @Override
    public void create() {

    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

    }



}
