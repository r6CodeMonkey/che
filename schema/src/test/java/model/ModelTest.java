package model;

import message.*;
import model.generic.GenericModel;
import org.junit.Test;
import util.Tags;

import javax.swing.text.html.HTML;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 11/12/15.
 */
public class ModelTest {

    public static final String ACKNOWLEDGE_TEST =  "{" + Tags.ACK_ID + ":'1', " + Tags.INFO + ":'info'," + Tags.STATE + ":'state'}";
    public static final String CORE_TEST = "{" + Tags.UID + ":'1'}";
    public static final String LOCATION_TEST = "{" + Tags.ALTITUDE + ":1.0," + Tags.SPEED + ":2.0," + Tags.LATITUTDE + ":3.0," + Tags.LONGITUDE + ":4.0," + Tags.UTM + ":'UTM'," + Tags.SUB_UTM + ":'SUB'}";
    public static final String USER_TEST = "{" + Tags.UID + ":'1'}";
    public static final String GENERIC_TEST = "{" + Tags.UID + ":'1'," + Tags.TYPE + ":'generic'," + Tags.VALUE + ":'value'," + Tags.STATE + ":'state'}";


    @Test
    public void testAcknowledge() {
        Acknowledge acknowledge = new Acknowledge(ACKNOWLEDGE_TEST);

        assertEquals("1", acknowledge.getAckId());
        assertEquals("info", acknowledge.getInfo());
        assertEquals("state", acknowledge.getState());

    }

    @Test
    public void testCore() {
        Core core = new Core(CORE_TEST);

        assertEquals("1", core.getUid());

    }

    @Test
    public void testLocation() {
        Location location = new Location(LOCATION_TEST);

        assertEquals("UTM", location.getUtm());
        assertEquals("SUB", location.getSubUtm());
        assertEquals(1.0, location.getAltitude(), 0);
        assertEquals(2.0, location.getSpeed(), 0);
        assertEquals(3.0, location.getLatitude(), 0);
        assertEquals(4.0, location.getLongitude(), 0);

    }

    @Test
    public void testUser() {
        User user = new User(USER_TEST);

        assertEquals("1", user.getUid());
    }

    @Test
    public void testGeneric() {
        GenericModel genericModel = new GenericModel(GENERIC_TEST);

        assertEquals("1", genericModel.getId());
        assertEquals("generic", genericModel.getType());
        assertEquals("state", genericModel.getState());
        assertEquals("value", genericModel.getValue());


    }
}
