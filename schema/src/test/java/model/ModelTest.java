package model;

import message.Acknowledge;
import message.Alliance;
import message.GameObject;
import message.*;
import message.Missile;
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
    public void testUTM() {

        UTM utm = new UTM(MessageTest.UTM);
        model.UTM utm1 = new model.UTM(utm);

        assertEquals("E1", utm1.getUtmLat());
        assertEquals("3W", utm1.getUtmLong());

    }

    @Test
    public void testAlliance() {

        Alliance alliance = new Alliance(MessageTest.ALLIANCE);
        model.Alliance alliance1 = new model.Alliance(alliance);

        assertEquals("15", alliance1.getKey());
        assertEquals("team", alliance1.name);
        assertEquals("hello", alliance1.value);
        assertEquals(Tags.ALLIANCE_POST, alliance1.state);
        assertEquals("Tim", alliance1.getMembers().get(0).name);


    }

    @Test
    public void testMissile() {

        message.Missile missile = new Missile(MessageTest.MISSILE);
        model.Missile missile1 = new model.Missile(missile);

        assertEquals(5000, missile1.range, 0);
        assertEquals(5, missile1.impactRadius, 0);
        assertEquals(6, missile1.radiusImpactScalar, 0);
        assertEquals(100, missile1.payLoad, 0);

        assertEquals(true, missile1.launched);
        assertEquals(false, missile1.destroyed);

        assertEquals("99", missile1.getKey());
        assertEquals(Tags.MISSILE_TARGET, missile1.state);
        assertEquals("fire", missile1.value);

        assertEquals("E1", missile1.currentUTMLocation.utm.getUtmLat());
        assertEquals("3W", missile1.currentUTMLocation.utm.getUtmLong());
        assertEquals("E1", missile1.startUTMLocation.utm.getUtmLat());
        assertEquals("3W", missile1.startUTMLocation.utm.getUtmLong());
        assertEquals("E1", missile1.targetUTMLocation.utm.getUtmLat());
        assertEquals("3W", missile1.targetUTMLocation.utm.getUtmLong());


    }

    @Test
    public void testGameObject() {

        GameObject gameObject = new GameObject(MessageTest.GAME_OBJECT);
        model.GameObject gameObject1 = new model.GameObject(gameObject);

        assertEquals("58", gameObject1.getKey());
        assertEquals(Tags.GAME_OBJECT_HIT, gameObject1.state);
        assertEquals("true", gameObject1.value);
        assertEquals(2098, gameObject1.mass, 0);
        assertEquals(123.2356, gameObject1.velocity, 0);
        assertEquals(9.99, gameObject1.acceleration, 0);
        assertEquals(Boolean.FALSE, gameObject1.destroyed);
        assertEquals(Boolean.FALSE, gameObject1.fixed);
        assertEquals(Boolean.TRUE, gameObject1.hit);
        assertEquals(Boolean.TRUE, gameObject1.located);
        assertEquals("99", gameObject1.getMissiles().get(0).getKey());
        assertEquals("E1", gameObject1.utmLocation.utm.getUtmLat());
        assertEquals("3W", gameObject1.utmLocation.utm.getUtmLong());


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
        assertEquals("E1", player1.utmLocation.utm.getUtmLat());
        assertEquals("3W", player1.utmLocation.utm.getUtmLong());

    }


}

