package util;

/**
 * Created by timmytime on 10/12/15.
 */
public class Tags {

    //object tags
    public static final String CORE = "core";
    public static final String LOCATION = "location";
    public static final String ACKNOWLEDGE = "acknowledge";
    public static final String PLAYER = "player";
    public static final String ALLIANCE = "alliance";
    public static final String MISSILE = "missile";
    public static final String GAME_OBJECT = "gameObject";

    //in out tags
    public static final String UID = "uid";  //can be used for both in and out.
    public static final String ACK_ID = "ackid";

    //int tags
    public static final String LONGITUDE = "long";
    public static final String LATITUTDE = "lat";
    public static final String SPEED = "speed";
    public static final String ALTITUDE = "altitude";

    //out tags
    public static final String STATE = "state";
    public static final String INFO = "info";
    public static final String UTM = "utm";
    public static final String SUB_UTM = "subutm";

    //generics
    public static final String TYPE = "type";
    public static final String VALUE = "value";

    //states
    public final static String ERROR = "ERROR";
    public final static String SUCCESS = "SUCCESS";
    public final static String ACCEPT = "ACCEPT";
    public final static String UUID = "UUID";

    //general
    public final static String ACTIVE = "ACTIVE";

    /*
    generic types.  game based message calls
     */
    //alliance
    public final static String ALLIANCE_CREATE = "CREATE_ALLIANCE_CREATE";
    public final static String ALLIANCE_JOIN = "JOIN_ALLIANCE_JOIN";
    public final static String ALLIANCE_LEAVE = "LEAVE_ALLIANCE_LEAVE";
    public final static String ALLIANCE_POST = "ALLIANCE_POST";
    public final static String ALLIANCE_INVITE = "ALLIANCE_INVITE";

    //objects ie weapons etc
    public final static String GAME_OBJECT_ADD = "OBJECT_ADD";
    public final static String GAME_OBJECT_MOVE = "OBJECT_MOVE";
    public final static String GAME_OBJECT_HIT = "OBJECT_HIT";
    public final static String GAME_OBJECT_DESTROYED = "OBJECT_DESTROYED";

    //launch commands
    public final static String MISSILE_TARGET = "MISSILE_TARGET";
    public final static String MISSILE_FIRE = "MISSILE_FIRE";
    public final static String MISSILE_CANCEL = "MISSILE_CANCEL";

    //General che object fields
    public final static String ALLIANCE_NAME = "allianceName";
    public final static String ALLIANCE_KEY = "allianceKey";
    public final static String ALLIANCE_MEMBERS = "allianceMembers";


}
