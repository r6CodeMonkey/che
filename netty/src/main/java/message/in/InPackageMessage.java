package message.in;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by timmytime on 09/02/15.
 */
public class InPackageMessage implements Serializable {

    public static final String PID = "pid";
    public static final String PLONG = "plong";
    public static final String PLAT = "plat";
    public static final String PSTAT = "pstat";
    public static final String PTYPE = "ptype";
    public static final String POWN = "pown";
    public static final String PCUR = "pcur";


    private String pid;
    private double latitude;
    private double longitude;
    private int type;
    private int status;
    private String owner;
    private String currentOwner;

    private InGridMessage gridMessage;


    public InPackageMessage(InGridMessage message) throws JSONException {

        JSONObject json = message.getCoreMessage().getInnerMessage();

        this.gridMessage = message;
        this.setPid(json.getString(PID));
        this.setType(json.getInt(PTYPE));
        this.setStatus(json.getInt(PSTAT));
        this.setLatitude(json.getDouble(PLAT));
        this.setLongitude(json.getDouble(PLONG));
        this.setOwner(json.getString(POWN));
        this.setCurrentOwner(json.getString(PCUR));
    }

    public String getPid() {
        return pid;
    }

    public String getOwner() {
        return owner;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setCurrentOwner(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    public InGridMessage getCoreMessage() {
        return gridMessage;
    }

}
