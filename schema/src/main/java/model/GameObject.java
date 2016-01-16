package model;

/**
 * Created by timmytime on 15/01/16.
 */
public class GameObject extends CoreModel {

    public String state, value;

    public GameObject(message.GameObject gameObject){

    }

    public GameObject(String key) {
        super(key);
    }

    @Override
    public String getMessage() {
        return null;
    }
}
