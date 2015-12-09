package model;

import model.Alliance;
import model.Grid;
import model.Player;
import util.StoreKey;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by timmytime on 04/05/15.
 */
public class ModelTest {

    private Alliance alliance;
    private Player player;

    @Test
    public void testCreateAlliance(){
        alliance = new Alliance("aid");
    }

    @Test
    public void testCreatePlayer(){

        player = new Player("player");

    }

    @Test
    public void testAlliance(){

        testCreateAlliance();
        testCreatePlayer();

        assertEquals("aid", alliance.getAllianceKey().getStoreID());

        alliance.addPlayer(player);

        assertTrue(alliance.getPlayerKeys().containsKey(player.getPlayerKey().getStoreID()));

        alliance.removePlayer(player);

        assertFalse(alliance.getPlayerKeys().containsKey(player.getPlayerKey().getStoreID()));


    }

    @Test
    public void testGrid(){

        testCreatePlayer();
        Grid grid = new Grid("grid");

        assertEquals("grid", grid.getKey());

        grid.addPlayer(player);

        assertTrue(grid.getPlayers().containsKey(player.getPlayerKey().getStoreID()));

        grid.removePlayer(player);

        assertFalse(grid.getPlayers().containsKey(player.getPlayerKey().getStoreID()));

        //also do packages...


    }

    @Test
    public void testPackage(){

    }

    @Test
    public void testPlayer(){

        testCreatePlayer();

        assertEquals("player", player.getPlayerKey().getStoreID());

        player.setPlayerKey("regid");

        assertEquals("regid", player.getPlayerKey().getRegID());

        //need to test various store key adds.
        StoreKey storeKey = new StoreKey("store", "reg");

        player.addAllianceKey(storeKey);

        assertTrue(player.getAllianceKeys().containsKey("store"));

        player.removeAllianceKey(storeKey);

        assertFalse(player.getAllianceKeys().containsKey("store"));

        player.addGridKey(storeKey);

        assertNotNull(player.getGridKey("store"));

        player.removeGridKey(storeKey);

        assertNull(player.getGridKey("store"));

       //have others fuck it for time being.



    }
}
