package message;


/**
 * Created by timmytime on 15/01/16.
 */
public class GameObject extends CoreMessage {

    public GameObject() {
    }

    public GameObject(String message) {
        super(message);
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
