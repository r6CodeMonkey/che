package util;

/**
 * Created by timmytime on 10/12/15.
 */
public class Tags {

    //object tags
    public static final String ACKNOWLEDGE = "acknowledge";
    public static final String PLAYER = "player";
    public static final String ALLIANCE = "alliance";
    public static final String MISSILE = "missile";
    public static final String GAME_OBJECT = "gameObject";
    public static final String UTM = "utm";
    public static final String SUB_UTM = "subUtm";
    public static final String UTM_LOCATION = "utmLocation";
    public static final String CHE = "che";
    public static final String HAZELCAST = "hazelcast";


    //generics
    public static final String STATE = "state";
    public static final String INFO = "info";
    public static final String TYPE = "type";
    public static final String VALUE = "value";

    //state values
    public final static String ERROR = "ERROR";
    public final static String SUCCESS = "SUCCESS";
    public final static String ACCEPT = "ACCEPT";
    public final static String ACTIVE = "ACTIVE";
    public final static String PLAYER_ENTERED = "PLAYER_ENTERED";
    public final static String PLAYER_LEFT = "PLAYER_LEFT";

    //states
    public final static String UUID = "UUID";
    public final static String CONNECT = "CONNECT";
    public final static String MESSAGE = "MESSAGE";

    //acknowledge
    public static final String ACK_ID = "ackid";

    //utm location
    public static final String LONGITUDE = "long";
    public static final String LATITUTDE = "lat";
    public static final String SPEED = "speed";
    public static final String ALTITUDE = "altitude";

    //utm
    public static final String UTM_GRID = "utmGrid";
    public static final String SUB_UTM_GRID = "subutmGrid";

    public static final String UTM_LAT_GRID = "utmLat";
    public static final String UTM_LONG_GRID = "utmLong";


    //alliance
    public final static String ALLIANCE_CREATE = "ALLIANCE_CREATE";
    public final static String ALLIANCE_JOIN = "ALLIANCE_JOIN";
    public final static String ALLIANCE_LEAVE = "ALLIANCE_LEAVE";
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

    public final static String PLAYER_KEY = "playerKey";
    public final static String PLAYER_NAME = "playerName";
    public final static String PLAYER_IMAGE = "playerImage";


    public final static String MISSILE_KEY = "missileKey";
    public final static String MISSILE_PAYLOAD = "missilePayLoad";
    public final static String MISSILE_RADIUS = "missileRadius";
    public final static String MISSILE_RANGE = "missileRange";
    public final static String MISSILE_RADIUS_IMPACT_SCALAR = "missileRadiusImpactScalar";
    public final static String MISSILE_UTM_LOCATION = "missileUtmLocation";
    public final static String MISSILE_TARGET_UTM_LOCATION = "missileTargetUtmLocation";
    public final static String MISSILE_START_UTM_LOCATION = "missileStartUtmLocation";
    public final static String MISSILE_LAUNCHED = "missileLaunched";
    public final static String MISSILE_DESTROYED = "missileDestoyed";

    //need game object equivalent.....to be fair build test framework next.


}
