package factory;

import model.ModelTest;
import org.junit.Test;

/**
 * Created by timmytime on 11/12/15.
 */
public class FactoryTest {

    @Test
    public void testFactory() {

        MessageFactory.getMessage(MessageFactory.ACKNOWLEDGE, ModelTest.ACKNOWLEDGE_TEST);
        MessageFactory.getMessage(MessageFactory.CORE, ModelTest.CORE_TEST);
        MessageFactory.getMessage(MessageFactory.ACKNOWLEDGE, ModelTest.ACKNOWLEDGE_TEST);
        MessageFactory.getMessage(MessageFactory.USER, ModelTest.USER_TEST);
        MessageFactory.getMessage(MessageFactory.LOCATION, ModelTest.LOCATION_TEST);
        MessageFactory.getMessage(MessageFactory.GENERIC, ModelTest.GENERIC_TEST);

    }
}
