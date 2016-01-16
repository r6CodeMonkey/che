package model;


/**
 * Created by timmytime on 15/01/16.
 */
public class Acknowledge extends CoreModel {

    public String state;
    public String value;

    public Acknowledge(String key) {
        super(key);
    }

    public Acknowledge(message.Acknowledge acknowledge){
        super(acknowledge.getKey());
        state = acknowledge.getState();
        value = acknowledge.getValue();
    }


    @Override
    public String getMessage() {

        message.Acknowledge acknowledge = new message.Acknowledge();
        acknowledge.create();
        acknowledge.setKey(key);
        acknowledge.setValue(value);
        acknowledge.setState(state);

        return acknowledge.toString();
    }
}
