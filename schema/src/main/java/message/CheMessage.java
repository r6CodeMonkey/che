package message;

import factory.MessageFactory;
import model.CoreModel;

/**
 * Created by timmytime on 15/01/16.
 */
public class CheMessage extends CoreMessage {
    public CheMessage(String message) {
        super(message);
    }

    public CheMessage(CoreModel model) {
        super(model);
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

    public CoreMessage getMessage(String type) {
        return MessageFactory.getCheMessage(this, type);
    }

}
