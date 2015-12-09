package messages.out;

import com.oddymobstar.server.netty.message.in.CoreMessage;
import com.oddymobstar.server.netty.message.in.InAllianceMessage;
import com.oddymobstar.server.netty.message.in.InGridMessage;
import com.oddymobstar.server.netty.message.out.Acknowledge;
import com.oddymobstar.server.netty.message.out.Message;
import com.oddymobstar.server.netty.message.out.OutAllianceMessage;
import com.oddymobstar.server.netty.message.out.OutGridMessage;
import com.oddymobstar.server.netty.util.UTM;
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


    @Test
    public void testMessage(){

        try {
            Message message = new Message();
        }catch (JSONException jse){
            throw new AssertionError(jse.getMessage());
        }

    }

    @Test
    public void testAcknowledge(){

        try{
            Acknowledge acknowledge = new Acknowledge("ack", Acknowledge.ERROR, "uid", "oid", "test");

            assertEquals("ack", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.ACK_ID));
            assertEquals("uid", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.UID));
            assertEquals("oid", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.OID));
            assertEquals("test", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.INFO));

            acknowledge = new Acknowledge("ack", Acknowledge.ERROR, "uid", "oid","test","utm", "subutm");

            assertEquals("ack", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.ACK_ID));
            assertEquals("uid", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.UID));
            assertEquals("oid", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.OID));
            assertEquals("test", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.INFO));
            assertEquals("utm", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.UTM));
            assertEquals("subutm", acknowledge.getCoreMessage().getJSONObject(Acknowledge.ACKNOWLEDGE).getString(Acknowledge.SUB_UTM));


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
    public void testAlliance() {

        testCreateAllianceMessage();
        InGridMessage gridMessage = new InGridMessage(coreMessage, new UTMConvert());


        try {
            InAllianceMessage allianceMessage = new InAllianceMessage(gridMessage);


            OutAllianceMessage outAllianceMessage = new OutAllianceMessage(allianceMessage, "amid", "address", "msg");

            assertEquals("amid", outAllianceMessage.getCoreMessage().getJSONObject(OutAllianceMessage.ALLIANCE).getString(OutAllianceMessage.AMID));
            assertEquals("address", outAllianceMessage.getCoreMessage().getJSONObject(OutAllianceMessage.ALLIANCE).getString(OutAllianceMessage.ADDRESS));
            assertEquals("msg", outAllianceMessage.getCoreMessage().getJSONObject(OutAllianceMessage.ALLIANCE).getString(OutAllianceMessage.MSG));
            assertEquals("msg", outAllianceMessage.getCoreMessage().getJSONObject(OutAllianceMessage.ALLIANCE).getString(OutAllianceMessage.MSG));
            assertEquals(0.0, outAllianceMessage.getCoreMessage().getJSONObject(OutAllianceMessage.ALLIANCE).getDouble(OutAllianceMessage.LONGITUDE),1.0);
            assertEquals(0.0, outAllianceMessage.getCoreMessage().getJSONObject(OutAllianceMessage.ALLIANCE).getDouble(OutAllianceMessage.LATITUDE),1.0);
            assertEquals("N31", outAllianceMessage.getCoreMessage().getJSONObject(OutAllianceMessage.ALLIANCE).getString(OutAllianceMessage.GRID_UTM));
            assertEquals("1C0", outAllianceMessage.getCoreMessage().getJSONObject(OutAllianceMessage.ALLIANCE).getString(OutAllianceMessage.SUB_UTM));


        }catch (JSONException jse){
            throw new AssertionError(jse.getMessage());
        }

    }

    @Test
    public void testPackage(){

    }

    @Test
    public void testGrid(){
        try{
            OutGridMessage gridMessage = new OutGridMessage(new UTM("lat", "long"), new UTM("lat", "long"), 0.0,0.0, 0.0, 0.0,"key", "type","address", "msg");

            assertEquals("msg", gridMessage.getCoreMessage().getJSONObject(OutGridMessage.GRID).getString(OutGridMessage.MSG));
            assertEquals("address", gridMessage.getCoreMessage().getJSONObject(OutGridMessage.GRID).getString(OutGridMessage.ADDRESS));

            //TODO review the lat long on these we dont actually use them it looks broken
            //if anything writing tests after code is good code review....the irony

        } catch (JSONException jse){
        throw new AssertionError(jse.getMessage());
    }
    }
}
