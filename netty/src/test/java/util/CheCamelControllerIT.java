package util;

import model.CheControllerObject;
import model.Core;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 29/12/15.
 */
public class CheCamelControllerIT {

    private static CheCamelController cheCamelController;


    @BeforeClass
    public static void init() throws Exception{

        cheCamelController = new CheCamelController();
        cheCamelController.start();

    }

    @Test
    public void testRoute() throws Exception{


        CheControllerObject test = new CheControllerObject(null,null);
       //need a proper test with returned object.
    //    new Thread(new Runnable() {
    //        public void run() {
                    cheCamelController.getTemplate().sendBody("netty:tcp://localhost:8089", test);

//            }
 //       }).start();


    }


    @AfterClass
    public static void destroy() throws Exception {

        cheCamelController.stop();
    }


}