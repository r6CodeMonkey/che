import core.HazelcastManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Created by timmytime on 11/12/15.
 */
public class HazelcastManagerIT {


    @BeforeClass
    public static void init(){
        HazelcastManager.start();
    }

    /*
      need to actually test the server,,,,ie set up localhost listeners etc.
     */


    @AfterClass
    public static void destroy(){
        HazelcastManager.stop();
    }

}
