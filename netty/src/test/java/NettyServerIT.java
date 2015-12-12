import core.NettyServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Created by timmytime on 12/12/15.
 */
public class NettyServerIT {


    @BeforeClass
    public static void init() throws Exception {
        NettyServer.main(null);
    }

    @AfterClass
    public static void destroy(){

    }

}
