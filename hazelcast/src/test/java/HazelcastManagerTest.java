import core.HazelcastManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.NettyChannelHandler;

import static org.junit.Assert.assertTrue;

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
    }

    @Test
    public void testMap(){
        HazelcastManager.createMap("map");
    }


    @AfterClass
    public static void destroy(){
        HazelcastManager.stop();
    }

}
