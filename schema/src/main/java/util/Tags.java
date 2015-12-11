package util;

/**
 * Created by timmytime on 10/12/15.
 */
public class Tags {

    //object tags
    public static final String CORE_OBJECT = "core";
    public static final String LOCATION_OBJECT = "location";
    public static final String GENERIC_OBJECT = "generic";
    public static final String ACKNOWLEDGE_OBJECT = "acknowledge";
    public static final String USER_OBJECT = "user";

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
    public final static int ERROR = 0;
    public final static int SUCCESS = 1;
    public final static int ACCEPT = 2;

}
