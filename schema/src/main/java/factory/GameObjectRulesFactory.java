package factory;

import util.GameObjectRules;
import util.GameObjectTypes;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by timmytime on 21/02/16.
 */
public class GameObjectRulesFactory {

    private final ClassLoader classLoader;

    public GameObjectRulesFactory(){
         classLoader = getClass().getClassLoader();

    }

    //give a type we need to load the relevant xml file..

    public GameObjectRules getRules(int subType) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(GameObjectRules.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        switch (subType){
            case GameObjectTypes.RV:
                return (GameObjectRules)jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/atv_rules.xml").getFile()));
            case GameObjectTypes.MINI_DRONE:
                return (GameObjectRules)jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/mini_drone_rules.xml").getFile()));
            //need to flesh out the rest at some point...once done, can them simply update the xml, which is the point.
            case GameObjectTypes.TANK:
                return (GameObjectRules)jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/tank_rules.xml").getFile()));
            default:
                return null;
        }
    }

    public static void main(String[] args){

        GameObjectRulesFactory gameObjectRulesFactory = new GameObjectRulesFactory();
        try {
            GameObjectRules gameObjectRules = gameObjectRulesFactory.getRules(GameObjectTypes.RV);

            System.out.println("mass is "+gameObjectRules.getMass());

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }


}
