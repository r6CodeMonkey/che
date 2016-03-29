package controller.handler.che;

import controller.handler.UTMHandler;
import core.HazelcastManagerInterface;
import factory.CheChannelFactory;
import factory.GameObjectRulesFactory;
import message.CheMessage;
import model.GameEngineModel;
import model.GameObject;
import model.Player;
import model.UTM;
import org.json.JSONException;
import server.GameEngineInterface;
import util.Configuration;
import util.GameObjectRules;
import util.Tags;

import javax.xml.bind.JAXBException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

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
            case Tags.MISSILE_DESTROYED:
                missileDestroyed(player, gameObject);
                break;
            case Tags.GAME_OBJECT_REPAIR:
                objectRepair(player, gameObject);
                break;
            case Tags.GAME_OBJECT_REINFORCE:
                objectReinforce(player, gameObject);
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

        player.getGameObjects().get(gameObject.getKey()).getMissiles().stream().filter(missile -> gameObject.getMissiles().get(0).getKey().equals(missile.getKey())).forEach(missile -> {
            missile.targetUTMLocation = gameObject.getMissiles().get(0).targetUTMLocation;
            missile.state = Tags.MISSILE_TARGET;
        });

        CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));
    }

    private void missileCancelTarget(Player player, GameObject gameObject) {

    }


    private void missileRemoved(Player player, GameObject gameObject) {
        //remove missile from the game object...ie its been transferred elsewhere.

    }

    public void objectRepair(Player player, GameObject gameObject) throws JAXBException, RemoteException {
        //add to engine and repair on iteration.  so basically.
        gameObject.value = "";
        gameObject.state = Tags.GAME_OBJECT_REPAIR; //its already set but do it again.

        //stops it moving!.
        gameObject.destinationUTMLocation = gameObject.utmLocation;

        gameEngineInterface.addGameEngineModel(new GameEngineModel(player.getKey(),
                CheChannelFactory.getCheChannel(player.getKey()).getChannel().remoteAddress().toString(),
                gameObject, gameObjectRulesFactory.getRules(gameObject.subType)));


    }

    public void objectReinforce(Player player, GameObject gameObject) {
        //this is a purchase only event.  once purchased we update the model with new strength value and confirm to client to do the same.
    }


    private void missileFire(Player player, GameObject gameObject) throws JSONException, NoSuchAlgorithmException, RemoteException, JAXBException {


        configuration.getLogger().debug("missile fired " + gameObject.getKey());

        hazelcastManagerInterface.subscribe(gameObject.utmLocation.utm.getUtm() + gameObject.utmLocation.subUtm.getUtm(),
                gameObject.getKey(), player.getKey());

        gameEngineInterface.addGameEngineModel(new GameEngineModel(player.getKey(),
                CheChannelFactory.getCheChannel(player.getKey()).getChannel().remoteAddress().toString(),
                gameObject, gameObjectRulesFactory.getRules(gameObject.subType), true));

        CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));
    }


    private void objectAdd(Player player, GameObject gameObject) throws RemoteException, JSONException, NoSuchAlgorithmException, JAXBException {


        player.getGameObjects().put(gameObject.getKey(), gameObject);

        //always assign the same values...we dont want to add values more than once.
        gameObject.destinationUTMLocation = gameObject.utmLocation;

        utmHandler.handleUTMChange(gameObject.utmLocation, player);

        //attempt to subscribe.  mainly for buildings and anything deployed and not moved.
        hazelcastManagerInterface.subscribe(gameObject.utmLocation.utm.getUtm() + gameObject.utmLocation.subUtm.getUtm(), gameObject.getKey(), player.getKey());

        //it wont be moving so should be ok at present.  need to test this further!.
        gameEngineInterface.addGameEngineModel(new GameEngineModel(player.getKey(),
                CheChannelFactory.getCheChannel(player.getKey()).getChannel().remoteAddress().toString(),
                gameObject, gameObjectRulesFactory.getRules(gameObject.subType)));

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

            //attempt to subscribe to topic
            hazelcastManagerInterface.subscribe(gameObject.utmLocation.utm.getUtm() + gameObject.utmLocation.subUtm.getUtm(), gameObject.getKey(), player.getKey());

            gameEngineInterface.addGameEngineModel(new GameEngineModel(player.getKey(),
                    CheChannelFactory.getCheChannel(player.getKey()).getChannel().remoteAddress().toString(),
                    gameObject, gameObjectRulesFactory.getRules(gameObject.subType)));
        } else {
            gameObject.value = Tags.ERROR;  //just fail it...simples...
        }

        CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));

    }


    private void objectDestroyed(Player player, GameObject gameObject) throws JAXBException, RemoteException {

        player.getGameObjects().remove(gameObject.getKey());

        gameEngineInterface.removeGameEngineModel(new GameEngineModel(player.getKey(),
                CheChannelFactory.getCheChannel(player.getKey()).getChannel().remoteAddress().toString(),
                gameObject, gameObjectRulesFactory.getRules(gameObject.subType)));

    }

    private void missileDestroyed(Player player, GameObject gameObject) throws JAXBException, RemoteException {

        player.getGameObjects().get(gameObject.getKey()).getMissiles().stream().filter(missile -> gameObject.getMissiles().get(0).getKey().equals(missile.getKey())).forEach(missile -> {
            missile.state = Tags.MISSILE_DESTROYED;
        });  //dont delete them...may want reference in future etc.
        gameEngineInterface.removeGameEngineModel(new GameEngineModel(player.getKey(),
                CheChannelFactory.getCheChannel(player.getKey()).getChannel().remoteAddress().toString(),
                gameObject, gameObjectRulesFactory.getRules(gameObject.subType)));

    }

    private void objectHit(Player player, GameObject gameObject) throws RemoteException {

        player.getGameObjects().get(gameObject.getKey()).strength = gameObject.strength;
    }

}
