package store;

import com.oddymobstar.server.netty.model.Alliance;
import com.oddymobstar.server.netty.model.Player;
import com.oddymobstar.server.netty.store.HazelcastManager;
import com.oddymobstar.server.netty.util.UTM;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import stub.StubChannel;

import static org.junit.Assert.*;

/**
 * Created by timmytime on 04/05/15.
 *
 *  need to stub hazelcast....
 *
 */
public class StoreTest {

    private static HazelcastManager hazelcastManager;

    @BeforeClass
    public static void init(){

        hazelcastManager = new HazelcastManager();

    }

    @AfterClass
    public static void shutDown(){

        hazelcastManager.stop();
    }

    @Test
    public void testGridStore(){

        assertNotNull("utm map ", hazelcastManager.getGridStore().getGridMap(new UTM("T", "35")));

        hazelcastManager.getGridStore().addSubUtm(new UTM("T", "35"), new UTM("1", "C0"));

        assertNotNull("utm map ", hazelcastManager.getGridStore().getSubGridMap(new UTM("T", "35"), new UTM("1", "C0")));

    }

    @Test
    public void testPackageStore(){

    }

    @Test
    public void testAllianceStore(){
    //basically can not actually test the listeners...just add and remove
        Alliance alliance = new Alliance("alliance");

        hazelcastManager.addAlliance(alliance);

        assertNotNull("alliance", hazelcastManager.getAllianceMap("alliance"));

        hazelcastManager.removeAlliance("alliance");

        assertNull("alliance", hazelcastManager.getAllianceMap("alliance"));

    }

    @Test
    public void testChannelStore(){
        //maybe hard to do so..other than basics...coul stub.  not end of world.
        //get this calss correct though then we have options.
    }

    @Test
    public void testHazelcastManager(){

        assertNotNull("alliance store",hazelcastManager.getAllianceStore());
        assertNotNull("package store", hazelcastManager.getPackageStore());
        assertNotNull("grid store", hazelcastManager.getGridStore());

        Player player = new Player("player");
        UTM utm = new UTM("T", "35");
        UTM sub = new UTM("1", "C0");

        assertTrue("utm changed",player.hasUTMChanged(utm));
        assertTrue("sub utm changed",player.hasSubUTMChanged(sub));

        utm = new UTM("T", "36");
        sub = new UTM("1", "C1");

        assertTrue("utm changed", player.hasUTMChanged(utm));
        assertTrue("sub utm changed",player.hasSubUTMChanged(sub));


        hazelcastManager.addPlayer(player, new StubChannel());
      //  hazelcastManager.updatePlayer(player);

        assertNotNull("player", hazelcastManager.getPlayer(player));

        hazelcastManager.removePlayer(player);

        assertNull("player", hazelcastManager.getPlayer(player));


        //move player..
        //hazelcastManager.moveGrids(player);



    }
}
