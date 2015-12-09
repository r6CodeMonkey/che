package com.oddymobstar.server.netty.message.out;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 23/02/15.
 */
public interface MessageInterface {

    public JSONObject getMessage() throws JSONException;

    public JSONObject getCoreMessage() throws JSONException;
}
