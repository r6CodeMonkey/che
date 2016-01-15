import core.HazelcastManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.SimpleHandler;
import util.TopicSubscriptions;

import static org.junit.Assert.*;


/**
 * Created by timmytime on 11/12/15.
 */
public class HazelcastManagerTest {

    private static Object object;
    private static HazelcastManager hazelcastManager;

    /*
    need to fix these tests...ie need a new listener
     */

    @BeforeClass
    public static void init() {
        hazelcastManager = new HazelcastManager();
        HazelcastManager.start();
        object = new String("tim");
    }

    @AfterClass
    public static void destroy() {
        HazelcastManager.stop();
    }

    @Test
    public void testTopic() {
        hazelcastManager.createTopic("test");

        SimpleHandler simpleHandler = new SimpleHandler();
        TopicSubscriptions topicSubscriptions = new TopicSubscriptions();

        //topicSubscriptions.addSubscription("test", hazelcastManager.subscribe("test", simpleHandler));

        hazelcastManager.publish("test", "test message");
        try {
            this.wait(1000);
            assertEquals("test message", simpleHandler.getLastMessage().getMessageObject().toString());
        } catch (Exception e) {

        }

        //now unsubscribe.
        hazelcastManager.unSubscribe("topic", topicSubscriptions);
        assertNull(topicSubscriptions.getSubscription("topic"));

        hazelcastManager.publish("test", "test message 2");
        try {
            this.wait(1000);
            assertNotEquals("test message 2", simpleHandler.getLastMessage().getMessageObject().toString());
        } catch (Exception e) {

        }

    }

    @Test
    public void testMap() {
        hazelcastManager.createMap("map");
        hazelcastManager.put("map", "object", object);
        assertEquals("tim", hazelcastManager.get("map", "object"));
        hazelcastManager.remove("map", "object");
        assertNull(hazelcastManager.get("map", "object"));
        hazelcastManager.put("map", "object", object);
        hazelcastManager.put("map", "object2", object);
        hazelcastManager.removeAll("map");
        assertEquals(0, hazelcastManager.get("map").size());
    }

}
