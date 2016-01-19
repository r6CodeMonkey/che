package message;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONObject;
import org.junit.Test;
import util.Tags;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 16/01/16.
 */
public class MessageTest {

    public static final String ACKNOWLEDGE = "{" + Tags.ACKNOWLEDGE + " :{" + Tags.ACK_ID + ":'1'," + Tags.STATE + ":" + Tags.MESSAGE + "," + Tags.VALUE + ":" + Tags.SUCCESS + "}}";
    public static final String UTM = "{" + Tags.UTM + " :{" + Tags.UTM_LAT_GRID + ":'E1', " + Tags.UTM_LONG_GRID + ":'3W'}}";
    public static final String SUB_UTM = "{" + Tags.UTM + " :{" + Tags.UTM_LAT_GRID + ":'TT', " + Tags.UTM_LONG_GRID + ":'JJ'}}";
    public static final String UTM_LOCATION = "{" + Tags.UTM_LOCATION + " :{" + Tags.STATE + ":''," + Tags.VALUE + ":''," + Tags.LATITUTDE + ":1.0, " + Tags.LONGITUDE + ":2.0," + Tags.ALTITUDE + ":10," + Tags.SPEED + ": 12.2," + Tags.UTM + ":" + UTM + "," + Tags.SUB_UTM + ":" + SUB_UTM + "}}";
    public static final String PLAYER = "{" + Tags.PLAYER + " :{" + Tags.PLAYER_KEY + ":'2'," + Tags.PLAYER_NAME + ":'Tim'," + Tags.PLAYER_IMAGE + ":'image'," + Tags.UTM_LOCATION + ":" + UTM_LOCATION + "}}";
    public static final String ALLIANCE = "{" + Tags.ALLIANCE + " :{" + Tags.ALLIANCE_KEY + ":'15'," + Tags.ALLIANCE_NAME + ":'team'," + Tags.STATE + ":" + Tags.ALLIANCE_POST + "," + Tags.VALUE + ":'hello'," + Tags.ALLIANCE_MEMBERS + ":[" + PLAYER + "]}}";
    public static final String HAZELCAST = "{" + Tags.HAZELCAST + " :{" + HazelcastMessage.REMOTE_ADDRESS + ":'remote2'," + HazelcastMessage.CHE_OBJECT + ":{testing:'that'}}}";
    public static final String CHE = "{" + Tags.CHE + ":{" + Tags.UTM + ":" + UTM + "}}";
    //add these later i havent thought about what it needs.
    public static final String MISSILE = "{" + Tags.MISSILE + " :{" + Tags.MISSILE_KEY + ":'99'," + Tags.STATE + ":" + Tags.MISSILE_TARGET + "," + Tags.VALUE + ":'fire'," + Tags.MISSILE_RADIUS + ":5," + Tags.MISSILE_RADIUS_IMPACT_SCALAR + ":6," + Tags.MISSILE_PAYLOAD + ":100," +
            Tags.MISSILE_RANGE + ":5000," + Tags.MISSILE_DESTROYED + ":false," + Tags.MISSILE_LAUNCHED + ":true," + Tags.MISSILE_UTM_LOCATION + ":" + UTM_LOCATION + ", " + Tags.MISSILE_START_UTM_LOCATION + ":" + UTM_LOCATION + "," + Tags.MISSILE_TARGET_UTM_LOCATION + ":" + UTM_LOCATION + "}}";
    public static final String GAME_OBJECT = "{" + Tags.GAME_OBJECT + " :{"+Tags.GAME_OBJECT_KEY+":'58',"+Tags.STATE+":"+Tags.GAME_OBJECT_HIT+","+Tags.VALUE+":'true',"+Tags.GAME_OBJECT_MASS+":2098,"+Tags.GAME_OBJECT_ACCELERATION+":9.99,"+
            Tags.GAME_OBJECT_VELOCITY+":123.2356,"+Tags.GAME_OBJECT_IS_DESTROYED+":'false',"+Tags.GAME_OBJECT_IS_FIXED+":'false',"+Tags.GAME_OBJECT_IS_HIT+":'true',"+Tags.GAME_OBJECT_IS_LOCATED+":'true',"+Tags.GAME_OBJECT_UTM_LOCATION+":"+UTM_LOCATION+","+
            Tags.GAME_OBJECT_MISSILES+":["+MISSILE+"]}}";


    @Test
    public void testPlayer() {

        Player player = new Player(PLAYER);

        assertEquals("2", player.getKey());
        assertEquals("Tim", player.getName());
        assertEquals("image", player.getImage());
        assertEquals("E1", player.getUTMLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", player.getUTMLocation().getUTM().getUTMLongGrid());


        player = new Player();
        player.create();
        player.setKey("3");
        player.setName("wright");
        player.setImage("another");
        player.setUTMLocation(new UTMLocation(UTM_LOCATION));

        assertEquals("3", player.getKey());
        assertEquals("wright", player.getName());
        assertEquals("another", player.getImage());
        assertEquals("E1", player.getUTMLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", player.getUTMLocation().getUTM().getUTMLongGrid());

    }

    @Test
    public void testAlliance() {

        Alliance alliance = new Alliance(ALLIANCE);

        assertEquals("15", alliance.getKey());
        assertEquals("team", alliance.getName());
        assertEquals("hello", alliance.getValue());
        assertEquals(Tags.ALLIANCE_POST, alliance.getState());
        assertEquals("Tim", alliance.getMembers().get(0).getName());

        alliance = new Alliance();
        alliance.create();

        alliance.setName("hockey");
        alliance.setKey("12");
        alliance.setState(Tags.ALLIANCE_POST);
        alliance.setValue("goodbye");

        Player player = new Player();
        player.create();
        player.setName("wrong");
        List<Player> players = new ArrayList<>();
        players.add(player);
        alliance.setMembers(players);

        assertEquals("12", alliance.getKey());
        assertEquals("hockey", alliance.getName());
        assertEquals("goodbye", alliance.getValue());
        assertEquals(Tags.ALLIANCE_POST, alliance.getState());
        assertEquals("wrong", alliance.getMembers().get(0).getName());


    }

    @Test
    public void testMissile() {

        Missile missile = new Missile(MISSILE);

        assertEquals(5000, missile.getRange(), 0);
        assertEquals(5, missile.getImpactRadius(), 0);
        assertEquals(6, missile.getImpactRadiusScalar(), 0);
        assertEquals(100, missile.getPayLoad(), 0);

        assertEquals(true, missile.isLaunched());
        assertEquals(false, missile.isDestroyed());

        assertEquals("99", missile.getKey());
        assertEquals(Tags.MISSILE_TARGET, missile.getState());
        assertEquals("fire", missile.getValue());

        assertEquals("E1", missile.getCurrentUTMLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", missile.getCurrentUTMLocation().getUTM().getUTMLongGrid());
        assertEquals("E1", missile.getStartUTMLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", missile.getStartUTMLocation().getUTM().getUTMLongGrid());
        assertEquals("E1", missile.getTargetUTMLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", missile.getTargetUTMLocation().getUTM().getUTMLongGrid());


        missile = new Missile();
        missile.create();

        missile.setKey("100");
        missile.setState("new");
        missile.setValue("something");
        missile.setDestroyed(true);
        missile.setLaunched(false);
        missile.setRange(3000);
        missile.setImpactRadius(15);
        missile.setImpactRadiusScalar(2);
        missile.setPayLoad(98);

        missile.setCurrentUTMLocation(new UTMLocation(UTM_LOCATION));
        missile.setStartUTMLocation(new UTMLocation(UTM_LOCATION));
        missile.setTargetUTMLocation(new UTMLocation(UTM_LOCATION));


        assertEquals(3000, missile.getRange(), 0);
        assertEquals(15, missile.getImpactRadius(), 0);
        assertEquals(2, missile.getImpactRadiusScalar(), 0);
        assertEquals(98, missile.getPayLoad(), 0);

        assertEquals(false, missile.isLaunched());
        assertEquals(true, missile.isDestroyed());

        assertEquals("100", missile.getKey());
        assertEquals("new", missile.getState());
        assertEquals("something", missile.getValue());

        assertEquals("E1", missile.getCurrentUTMLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", missile.getCurrentUTMLocation().getUTM().getUTMLongGrid());
        assertEquals("E1", missile.getStartUTMLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", missile.getStartUTMLocation().getUTM().getUTMLongGrid());
        assertEquals("E1", missile.getTargetUTMLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", missile.getTargetUTMLocation().getUTM().getUTMLongGrid());


    }

    @Test
    public void testUTM() {

        message.UTM utm = new UTM(UTM);

        assertEquals("E1", utm.getUTMLatGrid());
        assertEquals("3W", utm.getUTMLongGrid());

        utm = new UTM();
        utm.create();
        utm.setUTMLatGrid("T1");
        utm.setUTMLongGrid("31W");

        assertEquals("T1", utm.getUTMLatGrid());
        assertEquals("31W", utm.getUTMLongGrid());

    }

    @Test
    public void testUTMLocation() {

        UTMLocation utmLocation = new UTMLocation(UTM_LOCATION);

        assertEquals(1.0, utmLocation.getLatitude(), 0);
        assertEquals(2.0, utmLocation.getLongitude(), 0);
        assertEquals(10, utmLocation.getAltitude(), 0);
        assertEquals(12.2, utmLocation.getSpeed(), 0);

        assertEquals("E1", utmLocation.getUTM().getUTMLatGrid());
        assertEquals("3W", utmLocation.getUTM().getUTMLongGrid());

        assertEquals("TT", utmLocation.getSubUTM().getUTMLatGrid());
        assertEquals("JJ", utmLocation.getSubUTM().getUTMLongGrid());

        utmLocation = new UTMLocation();
        utmLocation.create();

        utmLocation.setLatitude(99.1);
        utmLocation.setLongitude(-12.3);
        utmLocation.setSpeed(5.5);
        utmLocation.setAltitude(13.2);
        utmLocation.setUTM(new UTM(UTM));
        utmLocation.setSubUTM(new UTM(SUB_UTM));

        assertEquals(99.1, utmLocation.getLatitude(), 0);
        assertEquals(-12.3, utmLocation.getLongitude(), 0);
        assertEquals(13.2, utmLocation.getAltitude(), 0);
        assertEquals(5.5, utmLocation.getSpeed(), 0);

        assertEquals("E1", utmLocation.getUTM().getUTMLatGrid());
        assertEquals("3W", utmLocation.getUTM().getUTMLongGrid());

        assertEquals("TT", utmLocation.getSubUTM().getUTMLatGrid());
        assertEquals("JJ", utmLocation.getSubUTM().getUTMLongGrid());


    }

    @Test
    public void testGameObject() {


        GameObject gameObject = new GameObject(GAME_OBJECT);

        assertEquals("58", gameObject.getKey());
        assertEquals(Tags.GAME_OBJECT_HIT, gameObject.getState());
        assertEquals("true", gameObject.getValue());
        assertEquals(2098, gameObject.getMass(),0);
        assertEquals(123.2356, gameObject.getVelocity(),0);
        assertEquals(9.99, gameObject.getAcceleration(),0);
        assertEquals(Boolean.FALSE, gameObject.isDestroyed());
        assertEquals(Boolean.FALSE, gameObject.isFixed());
        assertEquals(Boolean.TRUE, gameObject.isHit());
        assertEquals(Boolean.TRUE, gameObject.isLocated());
        assertEquals("99", gameObject.getMissiles().get(0).getKey());
        assertEquals("E1", gameObject.getUtmLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", gameObject.getUtmLocation().getUTM().getUTMLongGrid());

        gameObject = new GameObject();
        gameObject.create();

        gameObject.setKey("58");
        gameObject.setState(Tags.GAME_OBJECT_HIT);
        gameObject.setValue("true");
        gameObject.setMass(2098);
        gameObject.setVelocity(123.2356);
        gameObject.setAcceleration(9.99);
        gameObject.setHit(true);
        gameObject.setDestroyed(false);
        gameObject.setFixed(false);
        gameObject.setLocated(true);
        gameObject.setUtmLocation(new UTMLocation(UTM_LOCATION));

        Missile missile = new Missile();
        missile.create();
        missile.setKey("99");
        List<Missile> missiles = new ArrayList<>();
        missiles.add(missile);
        gameObject.setMissiles(missiles);


        assertEquals("58", gameObject.getKey());
        assertEquals(Tags.GAME_OBJECT_HIT, gameObject.getState());
        assertEquals("true", gameObject.getValue());
        assertEquals(2098, gameObject.getMass(),0);
        assertEquals(123.2356, gameObject.getVelocity(),0);
        assertEquals(9.99, gameObject.getAcceleration(),0);
        assertEquals(Boolean.FALSE, gameObject.isDestroyed());
        assertEquals(Boolean.FALSE, gameObject.isFixed());
        assertEquals(Boolean.TRUE, gameObject.isHit());
        assertEquals(Boolean.TRUE, gameObject.isLocated());
        assertEquals("99", gameObject.getMissiles().get(0).getKey());
        assertEquals("E1", gameObject.getUtmLocation().getUTM().getUTMLatGrid());
        assertEquals("3W", gameObject.getUtmLocation().getUTM().getUTMLongGrid());


    }

    @Test
    public void testAcknowledge() {

        Acknowledge acknowledge = new Acknowledge(ACKNOWLEDGE);

        assertEquals("1", acknowledge.getKey());
        assertEquals(Tags.SUCCESS, acknowledge.getValue());
        assertEquals(Tags.MESSAGE, acknowledge.getState());

        acknowledge = new Acknowledge();
        acknowledge.create();
        acknowledge.setKey("2");
        acknowledge.setState(Tags.CONNECT);
        acknowledge.setValue(Tags.ACTIVE);

        assertEquals("2", acknowledge.getKey());
        assertEquals(Tags.ACTIVE, acknowledge.getValue());
        assertEquals(Tags.CONNECT, acknowledge.getState());


    }

    @Test
    public void testHazelcastMessage() {

        HazelcastMessage hazelcastMessage = new HazelcastMessage(HAZELCAST);

        assertEquals("remote2", hazelcastMessage.getRemoteAddress());
        assertEquals("that", hazelcastMessage.getCheObject().getString("testing"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("testing", "this");
        hazelcastMessage = new HazelcastMessage("remote", jsonObject);

        assertEquals("remote", hazelcastMessage.getRemoteAddress());
        assertEquals("this", hazelcastMessage.getCheObject().getString("testing"));

    }

    @Test
    public void testCheMessage() {

        CheMessage cheMessage = new CheMessage(CHE);

        // cheMessage.setMessage(Tags.ACKNOWLEDGE, new Acknowledge(ACKNOWLEDGE));
        // cheMessage.setMessage(Tags.ALLIANCE, new Alliance(ALLIANCE));

        //  System.out.print(cheMessage.toString());

        assertEquals("E1", ((UTM) cheMessage.getMessage(Tags.UTM)).getUTMLatGrid());

        cheMessage.setMessage(Tags.ALLIANCE, new Alliance(ALLIANCE));

        assertEquals("team", ((Alliance) cheMessage.getMessage(Tags.ALLIANCE)).getName());

        cheMessage.setMessage(Tags.UTM_LOCATION, new UTMLocation(UTM_LOCATION));

        assertEquals(1.0, ((UTMLocation) cheMessage.getMessage(Tags.UTM_LOCATION)).getLatitude(), 0);

        cheMessage.setMessage(Tags.PLAYER, new Player(PLAYER));


        assertEquals("Tim", ((Player) cheMessage.getMessage(Tags.PLAYER)).getName());

        //
        UTMLocation utmLocation = ((Player) cheMessage.getMessage(Tags.PLAYER)).getUTMLocation();
        model.UTMLocation utmLocation1 = new model.UTMLocation(utmLocation);


    }

}
