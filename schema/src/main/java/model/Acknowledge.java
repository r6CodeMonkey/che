package model;

import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;

import javax.swing.text.html.HTML;

/**
 * Created by timmytime on 10/12/15.
 */
public class Acknowledge extends JSONObject {

    private String ackId, state, info;

    public Acknowledge(String acknowledge) throws JSONException {
        super(acknowledge);
        setAll();
    }

    public Acknowledge(JSONObject acknowledge) throws JSONException {
        super(acknowledge);
        setAll();
    }

    private void setAll(){
        setAckId(this.getString(Tags.ACK_ID));
        setInfo(this.getString(Tags.INFO));
        setState(this.getString(Tags.STATE));

    }

    public String getAckId() {
        return ackId;
    }

    public void setAckId(String ackId) {
        this.ackId = ackId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
