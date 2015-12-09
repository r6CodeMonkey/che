package com.oddymobstar.server.netty.message.out;

import com.oddymobstar.server.netty.message.in.CoreMessage;
import com.oddymobstar.server.netty.util.UTM;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 20/02/15.
 */
public class OutGridMessage extends Message {

    public static final String GRID = "grid";
    public static final String MSG = "msg";

    public static final String PLAYER = "player";
    public static final String PACKAGE = "package";


    /*

    for testing we are sending movement of people into grids.  long term not such a good
    idea.  well?

    another busy little worker.  we need to send grid information of interest, or
    to start with.  we also need to limit damage flow.  but this is managed by grid we reside in
     */
    public OutGridMessage(CoreMessage coreMessage, UTM utm, UTM subUtm, String key, String type, String address, String msg) throws JSONException {

        JSONObject inner = new JSONObject();


        inner.put(MSG, msg);
        inner.put(ADDRESS, address);
        inner.put(GRID_UTM, utm.getUtm());
        inner.put(SUB_UTM, subUtm.getUtm());
        inner.put(LATITUDE, coreMessage.getLatitude());
        inner.put(LONGITUDE, coreMessage.getLongitude());
        inner.put(KEY, key);
        inner.put(TYPE, type);
        inner.put(ALTITUDE, coreMessage.getAltitude());
        inner.put(SPEED, coreMessage.getSpeed());

        getCoreMessage().put(GRID, inner);
    }

    public OutGridMessage(UTM utm, UTM subUtm, double latitude, double longitude, double speed, double altitude, String key, String type, String address, String msg) throws JSONException {

        JSONObject inner = new JSONObject();


        inner.put(MSG, msg);
        inner.put(ADDRESS, address);
        inner.put(GRID_UTM, utm.getUtm());
        inner.put(SUB_UTM, subUtm.getUtm());
        inner.put(LATITUDE, latitude);
        inner.put(LONGITUDE, longitude);
        inner.put(KEY, key);
        inner.put(TYPE, type);
        inner.put(ALTITUDE, altitude);
        inner.put(SPEED, speed);

        getCoreMessage().put(GRID, inner);

    }

}
