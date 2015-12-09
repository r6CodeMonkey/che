package com.oddymobstar.server.netty.message.out;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 20/02/15.
 */
public class Acknowledge extends Message {


    public final static int ERROR = 0;
    public final static int ACCEPT = 1;

    public static final String ACKNOWLEDGE = "acknowledge";
    public static final String UID = "uid";
    public static final String OID = "oid";
    public static final String ACK_ID = "ackid";
    public static final String STATE = "state";
    public static final String INFO = "info";
    public static final String UTM = "utm";
    public static final String SUB_UTM = "subutm";



    /*
      create a JSON
      {"ack" : {"uid","oid","state","info"}
     */

    public Acknowledge(String ackId, int type, String uid, String oid, String msg) throws JSONException {

        JSONObject inner = new JSONObject();

        inner.put(ACK_ID, ackId);
        inner.put(UID, uid);
        inner.put(OID, oid);
        inner.put(STATE, type == ERROR ? "error" : "success");
        inner.put(INFO, msg);
        inner.put(UTM, "");
        inner.put(SUB_UTM, "");


        getCoreMessage().put(ACKNOWLEDGE, inner);

    }

    public Acknowledge(String ackId, int type, String uid, String oid, String msg, String utm, String subUtm) throws JSONException {

        JSONObject inner = new JSONObject();

        inner.put(ACK_ID, ackId);
        inner.put(UID, uid);
        inner.put(OID, oid);
        inner.put(STATE, type == ERROR ? "error" : "success");
        inner.put(INFO, msg);
        inner.put(UTM, utm);
        inner.put(SUB_UTM, subUtm);


        getCoreMessage().put(ACKNOWLEDGE, inner);

    }


}
