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

    public GameObjectRulesFactory() {
        classLoader = getClass().getClassLoader();

    }

    //give a type we need to load the relevant xml file..

    public static void main(String[] args) {

        GameObjectRulesFactory gameObjectRulesFactory = new GameObjectRulesFactory();
        try {
            GameObjectRules gameObjectRules = gameObjectRulesFactory.getRules(GameObjectTypes.RV);

            System.out.println("mass is " + gameObjectRules.getMass());

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public GameObjectRules getRules(int subType) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(GameObjectRules.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        switch (subType) {
            case GameObjectTypes.RV:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/atv_rules.xml").getFile()));
            case GameObjectTypes.MINI_DRONE:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/mini_drone_rules.xml").getFile()));
            case GameObjectTypes.TANK:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/tank_rules.xml").getFile()));
            case GameObjectTypes.G2G:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/ground_to_ground_rules.xml").getFile()));
            case GameObjectTypes.A2A:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/air_to_air_rules.xml").getFile()));
            case GameObjectTypes.A2W:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/air_to_water_rules.xml").getFile()));
            case GameObjectTypes.CARPET:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/carpet_rules.xml").getFile()));
            case GameObjectTypes.CLUSTER:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/cluster_rules.xml").getFile()));
            case GameObjectTypes.G2A:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/ground_to_air_rules.xml").getFile()));
            case GameObjectTypes.A2G:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/air_to_ground_rules.xml").getFile()));
            case GameObjectTypes.GARRISON:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/garrison_rules.xml").getFile()));
            case GameObjectTypes.OUTPOST:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/outpost_rules.xml").getFile()));
            case GameObjectTypes.SATELLITE:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/satellite_rules.xml").getFile()));
            case GameObjectTypes.WATER_MINE:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/water_mine_rules.xml").getFile()));
            case GameObjectTypes.GROUND_MINE:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/ground_mine_rules.xml").getFile()));
            case GameObjectTypes.G2W:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/ground_to_water_rules.xml").getFile()));
            case GameObjectTypes.W2A:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/water_to_air_rules.xml").getFile()));
            case GameObjectTypes.W2G:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/water_to_ground_rules.xml").getFile()));
            case GameObjectTypes.W2W:
                return (GameObjectRules) jaxbUnmarshaller.unmarshal(new File(classLoader.getResource("xml/water_to_water_rules.xml").getFile()));
            default:
                return null;
        }
    }


}
