package model;

/**
 * Created by timmytime on 15/01/16.
 */
public class Missile extends CoreModel {

    public String state, value;

    public Missile(message.Missile missile){

    }

    public Missile(String key) {
        super(key);
    }

    @Override
    public String getMessage() {
        return null;
    }
}
