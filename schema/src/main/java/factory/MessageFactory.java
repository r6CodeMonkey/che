package factory;


import message.*;
import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 11/12/15.
 */
public class MessageFactory {


    public static JSONObject getMessage(CoreMessage coreMessage) {

        String type = coreMessage.getType();

        switch (type) {
            case Tags.ACKNOWLEDGE:
                return new Acknowledge(coreMessage.toString());
            case Tags.PLAYER:
                new Player(coreMessage.toString());
            case Tags.LOCATION:
                return new UTMLocation(coreMessage.toString());
            case Tags.MISSILE:
                return new Missile(coreMessage.toString());
            case Tags.GAME_OBJECT:
                return new GameObject(coreMessage.toString());
            case Tags.ALLIANCE:
                return new Alliance(coreMessage.toString());
            default:
                throw new RuntimeException("Unknown message type");
        }

    }


}
