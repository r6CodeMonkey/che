package message;

import model.CoreModel;

/**
 * Created by timmytime on 15/01/16.
 */
public class Alliance extends CoreMessage {

    private model.Alliance alliance;

    public Alliance(String message) {
        super(message);
    }

    public Alliance(CoreModel model) {
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
