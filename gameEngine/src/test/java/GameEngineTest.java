import engine.GameEnginePhysics;
import factory.GameObjectRulesFactory;
import model.GameEngineModel;
import model.GameObject;
import model.UTM;
import model.UTMLocation;
import org.junit.Assert;
import org.junit.Test;
import util.Configuration;
import util.GameObjectRules;
import util.GameObjectTypes;
import util.map.UTMConvert;

import javax.xml.bind.JAXBException;

/**
 * Created by timmytime on 12/03/16.
 */
public class GameEngineTest {


    private static final String PLAYER_KEY = "playerKey";
    private static final String GAME_OBJECT_KEY = "gameObjectKey";


    @Test
    public void testEnginePhysics() throws JAXBException {

        Configuration configuration = new Configuration();

        GameObject gameObject;
        GameObjectRules gameObjectRules;

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

        gameObject.setDistanceBetweenPoints(GameEnginePhysics.getHaversineDistance(utmLocation.latitude ,utmLocation.longitude , utmLocation2.latitude,utmLocation2.longitude));


        GameObjectRulesFactory gameObjectRulesFactory = new GameObjectRulesFactory();
        //set up the things we need...mainly a current lat long and dest lat long..so set a type here.
        gameObjectRules = gameObjectRulesFactory.getRules(gameObject.subType); //basically its the mass and velocity i think. but prepoulates from loader...

        GameEngineModel gameEngineModel = new GameEngineModel(PLAYER_KEY, "fake", gameObject, gameObjectRules);


        GameEnginePhysics.process(null, gameEngineModel, configuration.getUtmConvert(), configuration.getGameEngineDelta());

        configuration.getLogger().debug(gameEngineModel.getMessage().toString());



        }

    @Test
    public void testHaversine() {

        double distance = GameEnginePhysics.getHaversineDistance(50.0686, -5.7161, 58.6400, -3.0700);

       // configuration.getLogger().debug("the full haversine distance is " + distance);


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
}
