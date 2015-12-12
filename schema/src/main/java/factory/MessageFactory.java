package factory;

import message.*;
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


    public static String createAcknowledge(String ackId, String state, String info) {
        return model.Acknowledge.create(ackId, state, info).toString();
    }


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
            default:
                throw new RuntimeException("Unknown message type");
        }

    }


}
