package model;

import message.*;
import org.junit.Test;
import util.Tags;

import javax.swing.text.html.HTML;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 11/12/15.
 */
public class ModelTest {

    @Test
    public void testAcknowledge(){
        String message = "{"+Tags.ACK_ID+":'1', "+Tags.INFO+":'info',"+Tags.STATE+":'state'}";
        Acknowledge acknowledge = new Acknowledge(message);

        assertEquals( "1", acknowledge.getAckId());
        assertEquals("info", acknowledge.getInfo());
        assertEquals("state", acknowledge.getState());


    }

    @Test
    public void testCore(){
        String message = "{"+Tags.UID+":'1'}";
        Core core = new Core(message);

        assertEquals("1", core.getUid());

    }

    @Test
    public void testLocation(){
        String message = "{"+Tags.ALTITUDE+":1.0,"+Tags.SPEED+":2.0,"+ Tags.LATITUTDE+":3.0,"+Tags.LONGITUDE+":4.0,"+Tags.UTM+":'UTM',"+Tags.SUB_UTM+":'SUB'}";
        Location location = new Location(message);

        assertEquals("UTM", location.getUtm());
        assertEquals("SUB", location.getSubUtm());
        assertEquals(1.0, location.getAltitude(), 0);
        assertEquals(2.0, location.getSpeed(), 0);
        assertEquals( 3.0, location.getLatitude(), 0);
        assertEquals( 4.0,location.getLongitude(), 0);

    }

    @Test
    public void testUser(){
        String message = "{"+Tags.UID+":'1'}";
        User user = new User(message);

        assertEquals("1", user.getUid());
     }
}
