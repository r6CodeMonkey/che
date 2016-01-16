package model;

import message.*;
import message.Acknowledge;
import message.Alliance;
import message.Player;
import message.UTM;
import message.UTMLocation;
import org.junit.Test;
import util.Tags;

import static org.junit.Assert.assertEquals;

public class ModelTest {


    /*
     test we can build the models from the messages....simply enough.
     */

    @Test
    public void testAcknowledge() {

        message.Acknowledge acknowledge = new Acknowledge(MessageTest.ACKNOWLEDGE);

        model.Acknowledge acknowledge1 = new model.Acknowledge(acknowledge);

        assertEquals("1", acknowledge1.key);
        assertEquals(Tags.MESSAGE, acknowledge1.state);
        assertEquals(Tags.SUCCESS, acknowledge1.value);
    }

    @Test
    public void testUTM(){

        UTM utm = new UTM(MessageTest.UTM);
        model.UTM utm1 = new model.UTM(utm);

        assertEquals("E1", utm1.getUtmLat());
        assertEquals("3W", utm1.getUtmLong());

    }

    @Test
    public void testAlliance(){

        Alliance alliance = new Alliance(MessageTest.ALLIANCE);
        model.Alliance alliance1 = new model.Alliance(alliance);

        assertEquals("15", alliance1.getKey());
        assertEquals("team", alliance1.name);
        assertEquals("hello", alliance1.value);
        assertEquals(Tags.ALLIANCE_POST, alliance1.state);
        assertEquals("Tim", alliance1.getMembers().get(0).name);


    }

    @Test
    public void testMissile(){

    }

    @Test
    public void testGameObject(){

    }

    @Test
    public void tesUTMLocation() {

        UTMLocation utmLocation = new UTMLocation(MessageTest.UTM_LOCATION);

        model.UTMLocation utmLocation1 = new model.UTMLocation(utmLocation);

        assertEquals(1.0, utmLocation1.latitude, 0);
        assertEquals(2.0, utmLocation1.longitude, 0);
        assertEquals(10, utmLocation1.altitude, 0);
        assertEquals(12.2, utmLocation1.speed, 0);

        assertEquals("E1", utmLocation1.utm.getUtmLat());
        assertEquals("3W", utmLocation1.utm.getUtmLong());

        assertEquals("TT", utmLocation1.subUtm.getUtmLat());
        assertEquals("JJ", utmLocation1.subUtm.getUtmLong());


    }

    @Test
    public void testPlayer() {

        Player player = new Player(MessageTest.PLAYER);

        model.Player player1 = new model.Player(player);

        assertEquals("2", player1.getKey());
        assertEquals("Tim", player1.name);
       // assertEquals("image", player1.getImage());
        assertEquals("E1",player1.utmLocation.utm.getUtmLat());
        assertEquals("3W",player1.utmLocation.utm.getUtmLong());

    }



}

