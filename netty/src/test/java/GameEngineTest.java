import core.HazelcastManagerInterface;
import factory.GameObjectRulesFactory;
import game.GameEngine;
import model.GameEngineModel;
import game.GameEnginePhysics;
import model.GameObject;
import model.UTM;
import model.UTMLocation;
import org.junit.BeforeClass;
import org.junit.Test;
import util.CheCallbackClient;
import util.Configuration;
import util.GameObjectRules;
import util.GameObjectTypes;

import java.rmi.Naming;
import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 24/02/16.
 */
public class GameEngineTest {


    private static GameEngine gameEngine;
    private static HazelcastManagerInterface hazelcastManagerInterface;
    private static Configuration configuration;

    private static final String PLAYER_KEY = "playerKey";
    private static final String GAME_OBJECT_KEY = "gameObjectKey";
    private static GameObject gameObject;
    private static GameObjectRules gameObjectRules;

    @BeforeClass
    public static void init() throws Exception {
        configuration = new Configuration();
        hazelcastManagerInterface = (HazelcastManagerInterface) Naming.lookup(configuration.getHazelcastURL());
        hazelcastManagerInterface.addCallback(new CheCallbackClient(configuration));




        gameEngine = new GameEngine(hazelcastManagerInterface, configuration);


        /*
         we test by adding objects properly.
         */

        //String playerKey, GameObject gameObject, GameObjectRules gameObjectRules

        gameObject = new GameObject(GAME_OBJECT_KEY);

        gameObject.subType = GameObjectTypes.RV;
        UTM utm = new UTM("U", "31");
        UTM subUtm = new UTM("8", "V9");

        UTMLocation utmLocation = new UTMLocation();
        utmLocation.latitude = 50.0686;
        utmLocation.longitude = -5.7161;
        utmLocation.utm = utm;
        utmLocation.subUtm = subUtm;
        gameObject.utmLocation = utmLocation;


        utm = new UTM("U", "39");
        subUtm = new UTM("1", "V4");

        utmLocation.latitude = 58.6400;
        utmLocation.longitude = -3.0700;
        utmLocation.utm = utm;
        utmLocation.subUtm = subUtm;
        gameObject.destinationUTMLocation = utmLocation;


        GameObjectRulesFactory gameObjectRulesFactory = new GameObjectRulesFactory();
        //set up the things we need...mainly a current lat long and dest lat long..so set a type here.
        gameObjectRules = gameObjectRulesFactory.getRules(gameObject.subType); //basically its the mass and velocity i think. but prepoulates from loader...

        GameEngineModel gameEngineModel = new GameEngineModel(PLAYER_KEY, gameObject, gameObjectRules);

        gameEngine.addGameEngineModel(gameEngineModel);


    }

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

    @Test
    public void testProcessPositions() throws RemoteException {

       //we hazelcast is running perse...really need another connection to it.

        gameEngine.processPositions();

    }
/*
    @Test
    public void testUpdateMaps() throws RemoteException {

        gameEngine.updateMaps();

    }

    @Test
    public void testProcessMissiles() throws RemoteException {

        gameEngine.processMissiles();

    }
    */
}
