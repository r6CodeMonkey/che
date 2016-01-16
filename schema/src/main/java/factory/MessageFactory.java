package factory;


import message.*;
import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 11/12/15.
 */
public class MessageFactory {


    public static JSONObject getMessage(String message) {

        JSONObject coreMessage = new JSONObject(message);
        String type = coreMessage.get(Tags.TYPE).toString();

        switch (type) {
            case Tags.ACKNOWLEDGE:
                return new Acknowledge(coreMessage.getJSONObject(Tags.ACKNOWLEDGE).toString());
            case Tags.PLAYER:
                new Player(coreMessage.getJSONObject(Tags.PLAYER).toString());
            case Tags.UTM_LOCATION:
                return new UTMLocation(coreMessage.getJSONObject(Tags.UTM_LOCATION).toString());
            case Tags.MISSILE:
                return new Missile(coreMessage.getJSONObject(Tags.MISSILE).toString());
            case Tags.GAME_OBJECT:
                return new GameObject(coreMessage.getJSONObject(Tags.GAME_OBJECT).toString());
            case Tags.ALLIANCE:
                return new Alliance(coreMessage.getJSONObject(Tags.ALLIANCE).toString());
            default:
                throw new RuntimeException("Unknown message type");
        }

    }

    public static CoreMessage getCheMessage(String coreMessage, String type) {

        switch (type) {
            case Tags.ACKNOWLEDGE:
                return new Acknowledge(coreMessage);
            case Tags.PLAYER:
                new Player(coreMessage);
            case Tags.UTM_LOCATION:
                return new UTMLocation(coreMessage);
            case Tags.UTM:
                return new UTM(coreMessage);
            case Tags.MISSILE:
                return new Missile(coreMessage);
            case Tags.GAME_OBJECT:
                return new GameObject(coreMessage);
            case Tags.ALLIANCE:
                return new Alliance(coreMessage);
            default:
                throw new RuntimeException("Unknown message type");
        }
    }


}
