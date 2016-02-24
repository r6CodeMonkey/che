import game.GameEnginePhysics;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 24/02/16.
 */
public class GameEngineTest {


    @Test
    public void testHaversine(){

        //lands end to john o groats...
        assertEquals(968202.3220386797, GameEnginePhysics.getHaversineDistance(50.0686, -5.7161, 58.6400, -3.0700), 0);
        assertEquals(-168.7117510737394, GameEnginePhysics.calculateBearing(58.6400, -3.0700, 50.0686, -5.7161), 0);  //the bearing is wrong somehow.  further review required.
        //stop and get dinner now.
        assertEquals(51.115329702300016, GameEnginePhysics.getLatitude(50.0686, 200000, -168.7117510737394), 0);
        assertEquals(-3.0048450967195777, GameEnginePhysics.getLongitude(50.0686,-5.7161,51.115329702300016,200000, -168.7117510737394), 0);
        //should add more at some point...

    }
}
