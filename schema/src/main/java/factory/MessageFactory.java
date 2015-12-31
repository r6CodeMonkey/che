package factory;

import message.receive.UTM;
import message.send.*;
import message.send.Acknowledge;
import message.send.Core;
import message.send.Location;
import message.send.User;
import org.json.JSONObject;

/**
 * Created by timmytime on 11/12/15.
 */
public class MessageFactory {

    public static final int ACKNOWLEDGE = 0;
    public static final int USER = 1;
    public static final int LOCATION = 2;
    public static final int CORE = 3;
    public static final int GENERIC = 4;
    public static final int UTM = 5;


    /*
      helper to make an acknowledge.  sent from server, should not be used from clients.
     */
    public static String createAcknowledge(String ackId, String state, String info) {
        return model.client.Acknowledge.create(ackId, state, info).toString();
    }


    /*
      not of huge use, as all messages will be in core anyway.  primarily for testing purposes (when type != CORE)
     */
    public static JSONObject getMessage(int type, String message) {

        switch (type) {
            case ACKNOWLEDGE:
                Acknowledge acknowledge = new Acknowledge();
                acknowledge.create(message);
                return acknowledge.get();
            case USER:
                User user = new User();
                user.create(message);
                return user.get();
            case LOCATION:
                Location location = new Location();
                location.create(message);
                return location.get();
            case CORE:
                Core core = new Core();
                core.create(message);
                return core.get();
            case GENERIC:
                GenericMessage genericMessage = new GenericMessage();
                genericMessage.create(message);
                return genericMessage.get();
            case UTM:
                message.receive.UTM utm = new UTM();
                utm.create(message);
                return utm.get();
            default:
                throw new RuntimeException("Unknown message type");
        }

    }


}
