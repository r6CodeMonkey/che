import core.HazelcastManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by timmytime on 11/12/15.
 */
public class HazelcastManagerTest {

    private static HazelcastManager hazelcastManager;

    @BeforeClass
    public static void init(){
        hazelcastManager = new HazelcastManager();
    }

    @Test
    public void testTopic(){

        hazelcastManager.createTopic("test");

    }

    @Test
    public void testMap(){

        hazelcastManager.createMap("map");

    }



    @AfterClass
    public static void destroy(){
        hazelcastManager.stop();
    }

}
