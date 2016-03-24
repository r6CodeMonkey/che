package controller.handler.che;

import controller.CheController;
import controller.handler.UTMHandler;
import core.HazelcastManagerInterface;
import factory.CheChannelFactory;
import factory.GameObjectRulesFactory;
import message.CheMessage;
import model.*;
import org.json.JSONException;
import server.GameEngineInterface;
import util.Configuration;
import util.GameObjectRules;
import util.Tags;

import javax.xml.bind.JAXBException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 13/01/16.
 */
public class GameObjectHandler {

    /*

      this could basically be

      Tank
      Missile launcher
      Ship
      Submarine
      Plane (airbourne).

      key is we need to consider the other states actions, basic physics should help

      probably need a game object type (ie state and value in use).

     */

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final GameEngineInterface gameEngineInterface;
    private final Configuration configuration;
    private final UTMHandler utmHandler;
    private final GameObjectRulesFactory gameObjectRulesFactory = new GameObjectRulesFactory();



    public GameObjectHandler(HazelcastManagerInterface hazelcastManagerInterface, GameEngineInterface gameEngineInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.gameEngineInterface = gameEngineInterface;
        this.configuration = configuration;
        this.utmHandler = new UTMHandler(hazelcastManagerInterface, configuration);
    }

    public void handle(Player player, GameObject gameObject) throws JSONException, NoSuchAlgorithmException, RemoteException, JAXBException {

        switch (gameObject.state) {
            case Tags.PURCHASE:
                purchaseGameObject(player, gameObject);
                break;
            case Tags.GAME_OBJECT_ADD:
                objectAdd(player, gameObject);
                break;
            case Tags.GAME_OBJECT_DESTROYED:
                objectDestroyed(player, gameObject);
                break;
            case Tags.GAME_OBJECT_HIT:
                objectHit(player, gameObject);
                break;
            case Tags.GAME_OBJECT_MOVE:
                objectMove(player, gameObject);
                break;
            case Tags.MISSILE_ADDED:
                missileAdded(player, gameObject);
                break;
            case Tags.MISSILE_REMOVED:
                missileRemoved(player, gameObject);
                break;
            case Tags.GAME_OBJECT_STOP:
                objectStop(player, gameObject);
                break;
            case Tags.MISSILE_TARGET:
                missileTarget(player, gameObject);
                break;
            case Tags.MISSILE_CANCEL:
                missileCancelTarget(player, gameObject);
            case Tags.MISSILE_FIRE:
                missileFire(player, gameObject);
                break;
        }
    }

    private void purchaseGameObject(Player player, GameObject gameObject) throws NoSuchAlgorithmException, JSONException, JAXBException {

        GameObjectRules gameObjectRules = gameObjectRulesFactory.getRules(gameObject.subType);  //reading files, but object is same on purchase.

        while (gameObject.quantity-- > 0) {

            //we add the object to db and tell user....
            gameObject.setKey(configuration.getUuidGenerator().generateKey("game object " + gameObject.type + "-" + gameObject.subType + "-" + gameObject.quantity + "-" + player.getKey()));

            //set the game object rules as we need them on server..
            gameObject.maxSpeed = gameObjectRules.getMaxSpeed();
            gameObject.mass = gameObjectRules.getMass();
            gameObject.impactRadius = gameObjectRules.getImpactRadius();
            gameObject.range = gameObjectRules.getMaxRange();
            gameObject.strength = gameObjectRules.getStrength();
            gameObject.force = gameObjectRules.getForce();


            configuration.getLogger().debug("creating a purchase game object " + gameObject.getKey());

            //need to add this to the player...
            player.getGameObjects().put(gameObject.getKey(), gameObject);

            CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));
        }
    }

    private void missileAdded(Player player, GameObject gameObject) throws RemoteException, JSONException, NoSuchAlgorithmException {

        player.getGameObjects().get(gameObject.getKey()).getMissiles().add(gameObject.getMissiles().get(0));

        gameObject.value = Tags.SUCCESS;
        CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));
    }

    private void missileTarget(Player player, GameObject gameObject) throws JSONException, NoSuchAlgorithmException {

        configuration.getLogger().debug("missile target set " + gameObject.getKey());

        for(Missile missile : player.getGameObjects().get(gameObject.getKey()).getMissiles()){
            if(gameObject.getMissiles().get(0).getKey().equals(missile.getKey())){
                missile.targetUTMLocation = gameObject.getMissiles().get(0).targetUTMLocation;
                missile.state = Tags.MISSILE_TARGET;
            }
        }

        CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));
    }

    private void missileCancelTarget(Player player, GameObject gameObject){

    }


    private void missileRemoved(Player player, GameObject gameObject) {
        //remove missile from the game object...ie its been transferred elsewhere.

    }


    private void missileFire(Player player, GameObject gameObject) throws JSONException, NoSuchAlgorithmException, RemoteException, JAXBException {


        configuration.getLogger().debug("missile fired " + gameObject.getKey());


        gameEngineInterface.addGameEngineModel(new GameEngineModel(player.getKey(),
                CheChannelFactory.getCheChannel(player.getKey()).getChannel().remoteAddress().toString(),
                gameObject, gameObjectRulesFactory.getRules(gameObject.subType), true));

        CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));
    }


    private void objectAdd(Player player, GameObject gameObject) throws RemoteException, JSONException, NoSuchAlgorithmException {


        player.getGameObjects().put(gameObject.getKey(), gameObject);

        /*
         soln.  added topic subscriptions to models as well so seperate from player subscriptions..simples.
         */

        //how do we unsubscribe?  i guess if we move out of zone, but then...
        //we may still be in zone as player and object not....regardless its ok to listen to sectors.
        //perhaps add something to player handler to remove pointless subscribes.
        utmHandler.handleUTMChange(gameObject.utmLocation, player);
        //need to consider do we put it in a UTM / SubUTM....probably should register our item to that topic.  makes sense.
        //and remove when we get move it out of zone.

        CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));


    }

    private void objectStop(Player player, GameObject gameObject) throws RemoteException, JAXBException, JSONException, NoSuchAlgorithmException {
        //update our game object
        configuration.getLogger().debug("game object stop");
        //need to set the object current distance to zero.  which
        //and set our distance to zero.
       //no engine will do this..with latest information CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));

    }

    private void objectMove(Player player, GameObject gameObject) throws RemoteException, JSONException, NoSuchAlgorithmException, JAXBException {


        //given our current dest cords..
        UTM utm = configuration.getUtmConvert().getUTMGrid(gameObject.destinationUTMLocation.latitude, gameObject.destinationUTMLocation.longitude);
        UTM subUtm = configuration.getUtmConvert().getUTMSubGrid(utm, gameObject.destinationUTMLocation.latitude, gameObject.destinationUTMLocation.longitude);


        boolean valid = false;

        for (UTM validator : gameObject.getDestinationValidator()) {

            if ((validator.getUtm()).equals(subUtm.getUtmLat() + subUtm.getUtmLong())) {
                valid = true;
            }
        }

        if (valid) {

            player.getGameObjects().get(gameObject.getKey()).destinationUTMLocation = gameObject.destinationUTMLocation;
            gameObject.value = Tags.SUCCESS;

            gameEngineInterface.addGameEngineModel(new GameEngineModel(player.getKey(),
                    CheChannelFactory.getCheChannel(player.getKey()).getChannel().remoteAddress().toString(),
                    gameObject, gameObjectRulesFactory.getRules(gameObject.subType)));
        } else {
            gameObject.value = Tags.ERROR;  //just fail it...simples...
        }

        CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));

    }

    private void objectHit(Player player, GameObject gameObject) {


        // publish to utm/ subutm grid a hit has occured


    }

    private void objectDestroyed(Player player, GameObject gameObject) {


        //   publish to utm / sub utm object destroyed and remove object
    }
}
