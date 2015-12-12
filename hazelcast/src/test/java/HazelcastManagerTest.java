import core.HazelcastManager;
import io.netty.channel.Channel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.NettyChannelHandler;
import util.TopicSubscriptions;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;


/**
 * Created by timmytime on 11/12/15.
 */
public class HazelcastManagerTest {

    private static Object object;

    @BeforeClass
    public static void init(){
        HazelcastManager.start();
        object = new String("tim");
    }

    @Test
    public void testTopic(){
        HazelcastManager.createTopic("test");

        NettyChannelHandler nettyChannelHandler = new NettyChannelHandler(mock(Channel.class));
        TopicSubscriptions topicSubscriptions = new TopicSubscriptions();

        topicSubscriptions.addSubscription("test", HazelcastManager.subscribe("test", nettyChannelHandler));

        HazelcastManager.publish("test", "test message");
        try {
            this.wait(1000);
            assertEquals("test message", nettyChannelHandler.getLastMessage().getMessageObject().toString());
        }catch (Exception e){

        }

        //now unsubscribe.
        HazelcastManager.unSubscribe("topic", topicSubscriptions);
        assertNull(topicSubscriptions.getSubscription("topic"));

        HazelcastManager.publish("test", "test message 2");
        try {
            this.wait(1000);
            assertNotEquals("test message 2", nettyChannelHandler.getLastMessage().getMessageObject().toString());
        }catch (Exception e){

        }

    }

    @Test
    public void testMap(){
        HazelcastManager.createMap("map");
        HazelcastManager.put("map", "object", object);
        assertEquals("tim", HazelcastManager.get("map", "object"));
        HazelcastManager.remove("map", "object");
        assertNull(HazelcastManager.get("map", "object"));
        HazelcastManager.put("map", "object", object);
        HazelcastManager.put("map", "object2", object);
        HazelcastManager.removeAll("map");
        assertEquals(0, HazelcastManager.get("map").size());
    }


    @AfterClass
    public static void destroy(){
        HazelcastManager.stop();
    }

}
