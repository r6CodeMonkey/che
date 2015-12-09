package message.in;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by timmytime on 09/02/15.
 */
public class InAllianceMessage implements Serializable {

    public static final String AID = "aid";
    public static final String STAT = "stat";
    public static final String TYPE = "type";
    public static final String MSG = "msg";


    //types of message
    public static final String ALLIANCE_JOIN = "join";
    public static final String ALLIANCE_LEAVE = "leave";
    public static final String ALLIANCE_PUBLISH = "publish";
    public static final String ALLIANCE_CREATE = "create";

    public static final String ALLIANCE_GLOBAL = "global";


    private String aid;
    private String type;
    private String status;
    private String msg;


    private InGridMessage gridMessage;


    public InAllianceMessage(InGridMessage message) throws JSONException {
        JSONObject json = message.getCoreMessage().getInnerMessage();

        this.gridMessage = message;

        this.setAid(json.getString(AID));
        this.setType(json.getString(TYPE));
        this.setStatus(json.getString(STAT));
        this.setMsg(json.getString(MSG));
    }

    public String getAid() {
        return aid;
    }


    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public InGridMessage getGridMessage() {
        return gridMessage;
    }

}
