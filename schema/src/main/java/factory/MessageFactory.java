package factory;


import message.*;
import util.Tags;

/**
 * Created by timmytime on 11/12/15.
 */
public class MessageFactory {


    public static CoreMessage getCheMessage(String coreMessage, String type) {

        switch (type) {
            case Tags.ACKNOWLEDGE:
                return new Acknowledge(coreMessage);
            case Tags.PLAYER:
                return new Player(coreMessage);
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
            case Tags.CHE_ACKNOWLEDGE:
                return Acknowledge.create(Tags.CHE_ACKNOWLEDGE, coreMessage);
            default:
                throw new RuntimeException("Unknown message type");
        }
    }


}
