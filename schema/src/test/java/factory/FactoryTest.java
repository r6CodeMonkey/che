package factory;

import message.Core;
import model.Acknowledge;
import model.ModelTest;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 11/12/15.
 */
public class FactoryTest {

    @Test
    public void testFactory(){

        MessageFactory.getMessage(MessageFactory.ACKNOWLEDGE, ModelTest.ACKNOWLEDGE_TEST);
        MessageFactory.getMessage(MessageFactory.CORE, ModelTest.CORE_TEST);
        MessageFactory.getMessage(MessageFactory.ACKNOWLEDGE, ModelTest.ACKNOWLEDGE_TEST);
        MessageFactory.getMessage(MessageFactory.USER, ModelTest.USER_TEST);
        MessageFactory.getMessage(MessageFactory.LOCATION, ModelTest.LOCATION_TEST);
        MessageFactory.getMessage(MessageFactory.GENERIC, ModelTest.GENERIC_TEST);

    }
}
