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
            case Tags.LOCATION:
                return new UTMLocation(coreMessage.getJSONObject(Tags.LOCATION).toString());
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

    public static CoreMessage getCheMessage(CoreMessage coreMessage, String type) {

        switch (type) {
            case Tags.ACKNOWLEDGE:
                return new Acknowledge(coreMessage.getJSONObject(Tags.ACKNOWLEDGE).toString());
            case Tags.PLAYER:
                new Player(coreMessage.getJSONObject(Tags.PLAYER).toString());
            case Tags.LOCATION:
                return new UTMLocation(coreMessage.getJSONObject(Tags.LOCATION).toString());
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


}
