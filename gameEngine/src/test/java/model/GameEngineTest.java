package model;

import core.HazelcastManagerInterface;
import engine.GameEngine;
import engine.GameEnginePhysics;
import factory.GameObjectRulesFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Configuration;
import util.GameObjectRules;
import util.GameObjectTypes;

import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by timmytime on 24/02/16.
 */
public class GameEngineTest {


    private static final String PLAYER_KEY = "playerKey";
    private static final String GAME_OBJECT_KEY = "gameObjectKey";
    private static GameEngine gameEngine;
    private static HazelcastManagerInterface hazelcastManagerInterface;
    private static Configuration configuration;
    private static GameEngineModel gameEngineModel;

    @BeforeClass
    public static void init() throws Exception {
        configuration = new Configuration();
        hazelcastManagerInterface = (HazelcastManagerInterface) Naming.lookup(configuration.getHazelcastURL());
        //  hazelcastManagerInterface.addCallback(new CheCallbackClient(configuration));


        gameEngine = new GameEngine(hazelcastManagerInterface, configuration);

        GameObject gameObject;
        GameObjectRules gameObjectRules;

        /*
         we test by adding objects properly.
         */

        //String playerKey, GameObject gameObject, GameObjectRules gameObjectRules

        gameObject = new GameObject(GAME_OBJECT_KEY);

        gameObject.subType = GameObjectTypes.RV;

        UTMLocation utmLocation = new UTMLocation();
        utmLocation.latitude = 50.0686;
        utmLocation.longitude = -5.7161;
        UTM utm = configuration.getUtmConvert().getUTMGrid(utmLocation.latitude, utmLocation.longitude);
        UTM subUtm = configuration.getUtmConvert().getUTMSubGrid(utm, utmLocation.latitude, utmLocation.longitude);
        utmLocation.utm = utm;
        utmLocation.subUtm = subUtm;
        gameObject.utmLocation = utmLocation;


        UTMLocation utmLocation2 = new UTMLocation();

        utmLocation2.latitude = 58.6400;
        utmLocation2.longitude = -3.0700;
        utm = configuration.getUtmConvert().getUTMGrid(utmLocation2.latitude, utmLocation2.longitude);
        subUtm = configuration.getUtmConvert().getUTMSubGrid(utm, utmLocation2.latitude, utmLocation2.longitude);
        utmLocation2.utm = utm;
        utmLocation2.subUtm = subUtm;
        gameObject.destinationUTMLocation = utmLocation2;


        GameObjectRulesFactory gameObjectRulesFactory = new GameObjectRulesFactory();
        //set up the things we need...mainly a current lat long and dest lat long..so set a type here.
        gameObjectRules = gameObjectRulesFactory.getRules(gameObject.subType); //basically its the mass and velocity i think. but prepoulates from loader...

        gameEngineModel = new GameEngineModel(PLAYER_KEY, "fake", gameObject, gameObjectRules);

        gameEngineModel.getGameObject().setDistanceBetweenPoints(GameEnginePhysics.getHaversineDistance(gameEngineModel.getGameObject().utmLocation.latitude,
                gameEngineModel.getGameObject().utmLocation.longitude,
                gameEngineModel.getGameObject().destinationUTMLocation.latitude,
                gameEngineModel.getGameObject().destinationUTMLocation.longitude));


        configuration.getLogger().debug("the full distance is " + gameEngineModel.getGameObject().getDistanceBetweenPoints());

        gameEngine.addGameEngineModel(gameEngineModel);

        /*
          ideally we want to add lots more objects...
         */
        for (double lat = 0; lat < 1; lat += 0.1) {

            for (double lng = 3; lng < 4; lng += 0.1) {

                gameObject = new GameObject(GAME_OBJECT_KEY + lat);

                gameObject.subType = GameObjectTypes.RV;


                utmLocation = new UTMLocation();
                utmLocation.latitude = lat;
                utmLocation.longitude = lng;
                utm = configuration.getUtmConvert().getUTMGrid(utmLocation.latitude, utmLocation.longitude);
                subUtm = configuration.getUtmConvert().getUTMSubGrid(utm, utmLocation.latitude, utmLocation.longitude);
                utmLocation.utm = utm;
                utmLocation.subUtm = subUtm;
                gameObject.utmLocation = utmLocation;


                utmLocation2 = new UTMLocation();

                utmLocation2.latitude = lat + 1;
                utmLocation2.longitude = lng - 1;
                utm = configuration.getUtmConvert().getUTMGrid(utmLocation2.latitude, utmLocation2.longitude);
                subUtm = configuration.getUtmConvert().getUTMSubGrid(utm, utmLocation2.latitude, utmLocation2.longitude);
                utmLocation2.utm = utm;
                utmLocation2.subUtm = subUtm;
                gameObject.destinationUTMLocation = utmLocation2;

                gameEngineModel = new GameEngineModel(PLAYER_KEY, "fake", gameObject, gameObjectRules);

                gameEngineModel.getGameObject().setDistanceBetweenPoints(GameEnginePhysics.getHaversineDistance(gameEngineModel.getGameObject().utmLocation.latitude,
                        gameEngineModel.getGameObject().utmLocation.longitude,
                        gameEngineModel.getGameObject().destinationUTMLocation.latitude,
                        gameEngineModel.getGameObject().destinationUTMLocation.longitude));


                gameEngine.addGameEngineModel(gameEngineModel);

            }

        }

        configuration.getLogger().debug("created objects");


    }

    @AfterClass
    public static void removeData() throws RemoteException {

//also bust
    }

    @Test
    public void testHaversine() {

        double distance = GameEnginePhysics.getHaversineDistance(50.0686, -5.7161, 58.6400, -3.0700);

        configuration.getLogger().debug("the full haversine distance is " + distance);


        double bearing = GameEnginePhysics.calculateBearing(50.0686, -5.7161, 58.6400, -3.0700);
        double latitude = GameEnginePhysics.getLatitude(50.0686, 200000, bearing);
        double longitude = GameEnginePhysics.getLongitude(50.0686, -5.7161, latitude, 200000, bearing);

        //lands end to john o groats...
        Assert.assertEquals(968202.3220386797, distance, 0);
        Assert.assertEquals(9.131774011425927, bearing, 0);
        //stop and get dinner now.
        Assert.assertEquals(51.84355702963163, latitude, 0);
        Assert.assertEquals(-5.254128463003278, longitude, 0);
        //should add more at some point...

    }

    @Test
    public void testProcessPositions() throws RemoteException {

        //we hazelcast is running perse...really need another connection to it.
        gameEngine.engine();

        //need to add test cases later....using as harness at moment.

    }


}
