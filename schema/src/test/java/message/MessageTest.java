package message;

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

    public static final String ACKNOWLEDGE = "{"+Tags.ACKNOWLEDGE+" :{"+ Tags.ACK_ID+":'1',"+Tags.STATE+":"+Tags.MESSAGE+","+Tags.VALUE+":"+Tags.SUCCESS+"}}";
    public static final String UTM = "{"+Tags.UTM+" :{"+Tags.UTM_GRID+":'E1', "+Tags.SUB_UTM_GRID+":'3W'}}";
    public static final String UTM_LOCATION = "{"+Tags.UTM_LOCATION+" :{"+Tags.LATITUTDE+":1.0, "+Tags.LONGITUDE+":2.0,"+Tags.ALTITUDE+":10,"+Tags.SPEED+": 12.2,"+Tags.UTM+":"+UTM+"}}";
    public static final String PLAYER = "{"+Tags.PLAYER+" :{"+Tags.PLAYER_KEY+":'2',"+Tags.PLAYER_NAME+":'Tim',"+Tags.PLAYER_IMAGE+":'image',"+Tags.UTM_LOCATION+":"+UTM_LOCATION+"}}";
    public static final String ALLIANCE = "{"+Tags.ALLIANCE+" :{"+Tags.ALLIANCE_KEY+":'15',"+Tags.ALLIANCE_NAME+":'team',"+Tags.STATE+":"+Tags.ALLIANCE_POST+","+Tags.VALUE+":'hello',"+Tags.ALLIANCE_MEMBERS+":["+PLAYER+"]}}";
    public static final String HAZELCAST = "{"+Tags.HAZELCAST+" :{"+HazelcastMessage.REMOTE_ADDRESS+":'remote2',"+HazelcastMessage.CHE_OBJECT+":{testing:'that'}}}";
    public static final String CHE = "{"+Tags.CHE+":{"+Tags.TYPE+":"+Tags.UTM+","+Tags.CORE+":"+UTM+"}}";
    //add these later i havent thought about what it needs.
    public static final String MISSILE = "{"+Tags.MISSILE+" :{}}";
    public static final String GAME_OBJECT = "{"+Tags.GAME_OBJECT+" :{}}";


    @Test
    public void testPlayer() {

        Player player = new Player(PLAYER);

        assertEquals("2", player.getKey());
        assertEquals("Tim", player.getName());
        assertEquals("image", player.getImage());
        assertEquals("E1",player.getUTMLocation().getUTM().getUTMGrid());
        assertEquals("3W",player.getUTMLocation().getUTM().getSubUTMGrid());


        player = new Player();
        player.create();
        player.setKey("3");
        player.setName("wright");
        player.setImage("another");
        player.setUTMLocation(new UTMLocation(UTM_LOCATION));

        assertEquals("3", player.getKey());
        assertEquals("wright", player.getName());
        assertEquals("another", player.getImage());
        assertEquals("E1",player.getUTMLocation().getUTM().getUTMGrid());
        assertEquals("3W",player.getUTMLocation().getUTM().getSubUTMGrid());

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
    public void testMissile(){

        Missile missile = new Missile(MISSILE);
    }

    @Test
    public void testUTM(){

        message.UTM utm = new UTM(UTM);

        assertEquals("E1", utm.getUTMGrid());
        assertEquals("3W", utm.getSubUTMGrid());

        utm = new UTM();
        utm.create();
        utm.setUTMGrid("T1");
        utm.setSubUTMGrid("31W");

        assertEquals("T1", utm.getUTMGrid());
        assertEquals("31W", utm.getSubUTMGrid());

    }

    @Test
    public void testUTMLocation(){

        UTMLocation utmLocation = new UTMLocation(UTM_LOCATION);

        assertEquals(1.0, utmLocation.getLatitude(), 0);
        assertEquals(2.0, utmLocation.getLongitude(), 0);
        assertEquals(10, utmLocation.getAltitude(), 0);
        assertEquals(12.2, utmLocation.getSpeed(), 0);

        assertEquals("E1", utmLocation.getUTM().getUTMGrid());
        assertEquals("3W", utmLocation.getUTM().getSubUTMGrid());

        utmLocation = new UTMLocation();
        utmLocation.create();

        utmLocation.setLatitude(99.1);
        utmLocation.setLongitude(-12.3);
        utmLocation.setSpeed(5.5);
        utmLocation.setAltitude(13.2);
        utmLocation.setUTM(new UTM(UTM));

        assertEquals(99.1, utmLocation.getLatitude(), 0);
        assertEquals(-12.3, utmLocation.getLongitude(), 0);
        assertEquals(13.2, utmLocation.getAltitude(), 0);
        assertEquals(5.5, utmLocation.getSpeed(), 0);

        assertEquals("E1", utmLocation.getUTM().getUTMGrid());
        assertEquals("3W", utmLocation.getUTM().getSubUTMGrid());


    }

    @Test
    public void testGameObject(){

        GameObject gameObject = new GameObject(GAME_OBJECT);
    }

    @Test
    public void testAcknowledge(){

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
    public void testHazelcastMessage(){

        HazelcastMessage hazelcastMessage = new HazelcastMessage(HAZELCAST);

        assertEquals("remote2", hazelcastMessage.getRemoteAddress());
        assertEquals("that", hazelcastMessage.getCheObject().getString("testing"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("testing", "this");
        hazelcastMessage = new HazelcastMessage("remote",jsonObject);

        assertEquals("remote", hazelcastMessage.getRemoteAddress());
        assertEquals("this", hazelcastMessage.getCheObject().getString("testing"));

    }

    @Test
    public void testCheMessage(){

        CheMessage cheMessage = new CheMessage(CHE);

        assertEquals(Tags.UTM, cheMessage.getType());
        assertEquals("E1", ((UTM)cheMessage.getMessage()).getUTMGrid());
    }

}
