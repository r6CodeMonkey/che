package message.in;


import model.Player;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * Created by timmytime on 09/02/15.
 */
public class CoreMessage implements Serializable {

    public static final String CORE_OBJECT = "core";
    public static final String UID = "uid";
    public static final String TYPE = "type";
    public static final String LONG = "long";
    public static final String LAT = "lat";
    public static final String ACK_ID = "ackid";
    public static final String SPEED = "speed";
    public static final String ALTITUDE = "altitude";

    private String uniqueIdentifier;
    private double longitude;
    private double latitude;
    private String messageType;
    private String ackId;
    private double speed;
    private double altitude;


    public static final String PACKAGE_TYPE = "package";
    public static final String ALLIANCE_TYPE = "alliance";
    public static final String PLAYER_TYPE = "player";
    public static final String IMAGE_TYPE = "image";


    private JSONObject innerMessage;

    private Player player;


    public CoreMessage(String message) throws JSONException {

        JSONObject json = new JSONObject(message).getJSONObject(CORE_OBJECT);

        setUniqueIdentifier(json.getString(UID));
        setMessageType(json.getString(TYPE));
        setLongitude(json.getDouble(LONG));
        setLatitude(json.getDouble(LAT));
        setAckId(json.getString(ACK_ID));
        setSpeed(json.getDouble(SPEED));
        setAltitude(json.getDouble(ALTITUDE));


        switch (getMessageType()) {
            case ALLIANCE_TYPE:
                this.innerMessage = json.getJSONObject(ALLIANCE_TYPE);
                break;
            case PACKAGE_TYPE:
                this.innerMessage = json.getJSONObject(PACKAGE_TYPE);
                break;
            case PLAYER_TYPE:
                //we dont do anything for this yet.
                //      this.innerMessage = json.getJSONObject(PLAYER_TYPE);
                break;
            case IMAGE_TYPE:
                this.innerMessage = json.getJSONObject(IMAGE_TYPE);
                break;
        }

    }

    public JSONObject getInnerMessage() {
        return innerMessage;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public String getAckId() {
        return ackId;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setAckId(String ackId) {
        this.ackId = ackId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


}
