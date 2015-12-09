package messages.in;

import com.oddymobstar.server.netty.message.in.CoreMessage;
import com.oddymobstar.server.netty.message.in.InAllianceMessage;
import com.oddymobstar.server.netty.message.in.InGridMessage;
import com.oddymobstar.server.netty.message.in.InPackageMessage;
import com.oddymobstar.server.netty.util.UTMConvert;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by timmytime on 04/05/15.
 */
public class MessageTest {

    private CoreMessage coreMessage;
    private InGridMessage gridMessage;

    @Test
    public void testCreateMessage(){

        //we need to construct a core message to test it...
        JSONObject testMessage = new JSONObject();
        JSONObject inner = new JSONObject();
        try {

            inner.put(CoreMessage.ACK_ID, "ackid");
            inner.put(CoreMessage.LAT,"0.0");
            inner.put(CoreMessage.LONG,"0.0");
            inner.put(CoreMessage.TYPE,CoreMessage.ALLIANCE_TYPE);
            inner.put(CoreMessage.UID, "uid");
            inner.put(CoreMessage.ALLIANCE_TYPE, new JSONObject()); //put an empty inner in.


            testMessage.put(CoreMessage.CORE_OBJECT, inner);

            coreMessage = new CoreMessage(testMessage.toString());

        }catch (JSONException jse){
            throw new AssertionError(jse.getMessage());
        }

    }

    @Test
    public void testCreateAllianceMessage(){

        //we need to construct a core message to test it...
        JSONObject testMessage = new JSONObject();
        JSONObject inner = new JSONObject();
        try {

            inner.put(CoreMessage.ACK_ID, "ackid");
            inner.put(CoreMessage.LAT,"0.0");
            inner.put(CoreMessage.LONG,"0.0");
            inner.put(CoreMessage.TYPE,CoreMessage.ALLIANCE_TYPE);
            inner.put(CoreMessage.UID, "uid");
            JSONObject alliance = new JSONObject();


            alliance.put(InAllianceMessage.AID, "aid");
            alliance.put(InAllianceMessage.TYPE, InAllianceMessage.ALLIANCE_PUBLISH);
            alliance.put(InAllianceMessage.STAT, InAllianceMessage.ALLIANCE_GLOBAL);
            alliance.put(InAllianceMessage.MSG, "msg");


            inner.put(CoreMessage.ALLIANCE_TYPE, alliance); //put an empty inner in.


            testMessage.put(CoreMessage.CORE_OBJECT, inner);

            coreMessage = new CoreMessage(testMessage.toString());

        }catch (JSONException jse){
            throw new AssertionError(jse.getMessage());
        }

    }

    @Test
    public void testCreatePackageMessage(){

        //we need to construct a core message to test it...
        JSONObject testMessage = new JSONObject();
        JSONObject inner = new JSONObject();
        try {

            inner.put(CoreMessage.ACK_ID, "ackid");
            inner.put(CoreMessage.LAT,"0.0");
            inner.put(CoreMessage.LONG,"0.0");
            inner.put(CoreMessage.TYPE,CoreMessage.PACKAGE_TYPE);
            inner.put(CoreMessage.UID, "uid");

            JSONObject pack = new JSONObject();

            inner.put(CoreMessage.PACKAGE_TYPE, pack);


            testMessage.put(CoreMessage.CORE_OBJECT, inner);

            coreMessage = new CoreMessage(testMessage.toString());

        }catch (JSONException jse){
            throw new AssertionError(jse.getMessage());
        }

    }



    @Test
    public void testCoreMessage(){

        testCreateMessage();

        assertEquals("ackid", coreMessage.getAckId());
        assertEquals(0.0, coreMessage.getLatitude(),1.0);
        assertEquals(0.0, coreMessage.getLongitude(),1.0);
        assertEquals("uid", coreMessage.getUniqueIdentifier());


    }


    @Test
    public void testAllianceMessage(){

        testCreateAllianceMessage();
        gridMessage = new InGridMessage(coreMessage, new UTMConvert());

        try{
            InAllianceMessage allianceMessage = new InAllianceMessage(gridMessage);

            assertEquals("aid", allianceMessage.getAid());
            assertEquals("msg", allianceMessage.getMsg());
            assertEquals(InAllianceMessage.ALLIANCE_GLOBAL, allianceMessage.getStatus());
            assertEquals(InAllianceMessage.ALLIANCE_PUBLISH, allianceMessage.getType());

        }catch (JSONException jse){
            throw new AssertionError(jse.getMessage());
        }

    }

    @Test
    public void testPackageMessage(){

        testCreatePackageMessage();
        gridMessage = new InGridMessage(coreMessage, new UTMConvert());


    }

    @Test
    public void testGridMessage(){
        //at some point we should test the UTM converter logic etc....
        testCreateMessage();
        gridMessage = new InGridMessage(coreMessage, new UTMConvert());

        assertNotNull("UTM check",gridMessage.getUtm());
        assertNotNull("SUBUTM check",gridMessage.getSubUtm());


    }
}
