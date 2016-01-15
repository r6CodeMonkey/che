package model;


import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class Acknowledge extends CoreModel {

    public String state;
    public String value;

    public Acknowledge(String key) {
        super(key);
    }


    @Override
    public String getMessage() {

        message.Acknowledge acknowledge = new message.Acknowledge(this);

        acknowledge.put(Tags.STATE, state);
        acknowledge.put(Tags.VALUE, value);

        return acknowledge.toString();
    }
}
