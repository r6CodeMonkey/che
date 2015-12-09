package message.out;

import message.in.InAllianceMessage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 20/02/15.
 */
public class OutAllianceMessage extends Message {

    public final static String ALLIANCE = "alliance";
    public final static String AID = "aid";
    public final static String AMID = "amid";
    public final static String TYPE = "type";
    public final static String MSG = "msg";


    public OutAllianceMessage(InAllianceMessage allianceMessage, String amid, String address, String msg) throws JSONException {

        JSONObject inner = new JSONObject();

        inner.put(AID, allianceMessage.getAid());
        inner.put(MSG, msg);
        inner.put(ADDRESS, address);
        inner.put(AMID, amid);
        inner.put(TYPE, allianceMessage.getType());
        inner.put(GRID_UTM, allianceMessage.getGridMessage().getUtm().getUtm());
        inner.put(SUB_UTM, allianceMessage.getGridMessage().getSubUtm().getUtm());
        inner.put(LATITUDE, allianceMessage.getGridMessage().getCoreMessage().getLatitude());
        inner.put(LONGITUDE, allianceMessage.getGridMessage().getCoreMessage().getLongitude());
        //do we also show who the message is from?///would mean players would know
        //other players.  not important at moment, better not to know really.
        getCoreMessage().put(ALLIANCE, inner);
    }


}
