package util;

/**
 * Created by timmytime on 10/12/15.
 */
public class Tags {

    //object tags
    public static final String ACKNOWLEDGE = "acknowledge";
    public static final String CHE_ACKNOWLEDGE = "cheAcknowledge";
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
    public static final String TIME = "time";
    public static final String PURCHASE = "PURCHASE";
    public static final String RECEIVED = "Received";
    public static final String CHE_RECEIVED = "cheReceived";



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
    public static final String CHE_ACK_ID = "cheAckid";


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
    public final static String MISSILE_ADDED = "MISSILE_ADDED";
    public final static String MISSILE_REMOVED = "MISSILE_REMOVED";

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

    public final static String GAME_OBJECT_KEY = "gameObjectKey";
    public final static String GAME_OBJECT_TYPE = "gameObjectType";
    public final static String GAME_OBJECT_SUBTYPE = "gameObjectSubType";
    public final static String GAME_OBJECT_UTM_LOCATION = "gameObjectUtmLocation";
    public final static String GAME_OBJECT_DEST_UTM_LOCATION = "gameObjectDestUtmLocation";
    public final static String GAME_OBJECT_MASS = "gameObjectMass";
    public final static String GAME_OBJECT_ACCELERATION = "gameObjectAcceleration";
    public final static String GAME_OBJECT_VELOCITY = "gameObjectVelocity";
    public final static String GAME_OBJECT_IS_FIXED = "gameObjectFixed";
    public final static String GAME_OBJECT_IS_HIT = "gameObjectHit";
    public final static String GAME_OBJECT_IS_DESTROYED = "gameObjectDestroyed";
    public final static String GAME_OBJECT_IS_LOCATED = "gameObjectLocated";
    public final static String GAME_OBJECT_IS_MOVING = "gameObjectMoving";
    public final static String GAME_OBJECT_MISSILES = "gameObjectMissiles";
    public final static String GAME_OBJECT_QUANTITY = "gameObjectQuantity";
    public final static String GAME_OBJECT_DEST_VALIDATOR = "gameObjectDestValidator";



}
