import game.GameEnginePhysics;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 24/02/16.
 */
public class GameEngineTest {


    @Test
    public void testHaversine(){

        double distance = GameEnginePhysics.getHaversineDistance(50.0686, -5.7161, 58.6400, -3.0700);
        double bearing = GameEnginePhysics.calculateBearing(50.0686, -5.7161, 58.6400, -3.0700);
        double latitude = GameEnginePhysics.getLatitude(50.0686, 200000, bearing);
        double longitude = GameEnginePhysics.getLongitude(50.0686, -5.7161, latitude, 200000, bearing);

        //lands end to john o groats...
        assertEquals(968202.3220386797, distance , 0);
        assertEquals(9.131774011425927, bearing, 0);
        //stop and get dinner now.
        assertEquals(51.84355702963163, latitude, 0);
        assertEquals(-5.254128463003278, longitude, 0);
        //should add more at some point...

    }
}
