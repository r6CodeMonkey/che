package message;


import model.CoreModel;

/**
 * Created by timmytime on 15/01/16.
 */
public class GameObject extends CoreMessage {

    public GameObject(String message) {
        super(message);
    }

    public GameObject(CoreModel model) {
        super(model.getMessage());
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

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
