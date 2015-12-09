package message.in;

import org.json.JSONException;

/**
 * Created by timmytime on 13/07/15.
 */
public class InImageMessage{


    /*
      really we should implement a player, but this is primarily for testing
     */

    public static final String IMAGE  = "image";
    private String image;

    public InImageMessage(CoreMessage message) throws JSONException{
     image = message.getInnerMessage().getString(IMAGE);
    }

    public String getImage(){ return image;}



}
