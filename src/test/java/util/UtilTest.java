package util;

import com.oddymobstar.server.netty.util.StoreKey;
import com.oddymobstar.server.netty.util.UTM;
import com.oddymobstar.server.netty.util.UTMConvert;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by timmytime on 04/05/15.
 */
public class UtilTest {

    @Test
    public void testStoreKey(){
        //very simply
        StoreKey storeKey = new StoreKey("store", "reg");

        assertEquals("store", storeKey.getStoreID());
        assertEquals("reg", storeKey.getRegID());

        storeKey.setRegID("reg2");

        assertEquals("reg2", storeKey.getRegID());


    }

    @Test
    public void testUTM() {
        //simple
        UTM utm = new UTM("lat", "lng");

        assertEquals("lat",utm.getUtmLat());
        assertEquals("lng",utm.getUtmLong());
        assertEquals("latlng",utm.getUtm());


    }

    @Test
    public void testConfiguration(){
        //long..
        //i dont even have a configuration file yet.
        //once i do we can test it loads properly,
    }

    @Test
    public void testUUIDGenerator(){
        //pointless.....other than the algo works.
    }

    @Test
    public void testUTMConvert(){
        //need some strong cases for this when i cba...
        UTMConvert utmConvert = new UTMConvert();

        UTM utm = utmConvert.getUTMGrid(40.0, 25.0);
        assertEquals("T35", utm.getUtm());
        assertEquals("1C10", utmConvert.getUTMSubGrid(utm,40.0,25.0).getUtm());

        utm = utmConvert.getUTMGrid(-34.0, -34.0);
        assertEquals("H25", utm.getUtm());
        assertEquals("7C20", utmConvert.getUTMSubGrid(utm, -34.0, -34.0).getUtm());

        utm = utmConvert.getUTMGrid(0.0, 0.0);
        assertEquals("N31", utm.getUtm());
        assertEquals("1C0", utmConvert.getUTMSubGrid(utm,0.0,0.0).getUtm());



    }


}
