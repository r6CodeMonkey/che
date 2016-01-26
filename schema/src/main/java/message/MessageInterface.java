package message;

import org.json.JSONObject;

/**
 * Created by timmytime on 15/01/16.
 */
public interface MessageInterface {

    public void create();

    public JSONObject getContents();

    public String getKey();

    public void setKey(String key);

}
