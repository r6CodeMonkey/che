package controller.handler.che;

import controller.CheController;
import controller.handler.UTMHandler;
import core.HazelcastManagerInterface;
import factory.CheChannelFactory;
import message.CheMessage;
import model.GameObject;
import model.Player;
import org.json.JSONException;
import util.Configuration;
import util.Tags;

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
    private final Configuration configuration;
    private final UTMHandler utmHandler;


    public GameObjectHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
        this.utmHandler = new UTMHandler(hazelcastManagerInterface, configuration);
    }

    public void handle(Player player, GameObject gameObject) throws JSONException, NoSuchAlgorithmException, RemoteException {

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
                missileAdded(player, gameObject); //need to add a missile to our game object...
                break;
            case Tags.MISSILE_REMOVED:
                missileRemoved(player, gameObject);
                break;
        }
    }

    private void purchaseGameObject(Player player, GameObject gameObject) throws NoSuchAlgorithmException, JSONException {
        while (gameObject.quantity-- > 0) {

            //we add the object to db and tell user....
            gameObject.setKey(configuration.getUuidGenerator().generateKey("game object " + gameObject.type + "-" + gameObject.subType + "-" + gameObject.quantity + "-" + player.getKey()));

            configuration.getLogger().debug("creating a game object " + gameObject.getKey());

            //need to add this to the player...
            player.getGameObjects().put(gameObject.getKey(), gameObject);

            CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));
        }
    }

    private void missileAdded(Player player, GameObject gameObject) {

        //this is wrong...player.getGameObjects().put(gameObject.getKey(), gameObject);

    }

    private void missileRemoved(Player player, GameObject gameObject) {
        //remove missile from the game object...ie its been transferred elsewhere.

    }

    private void objectAdd(Player player, GameObject gameObject) throws RemoteException, JSONException, NoSuchAlgorithmException {

        hazelcastManagerInterface.put(CheController.OBJECT_MAP, gameObject.getKey(), gameObject);

        player.getGameObjects().put(gameObject.getKey(), gameObject);
        //how do we unsubscribe?  i guess if we move out of zone, but then...
        //we may still be in zone as player and object not....regardless its ok to listen to sectors.
        //perhaps add something to player handler to remove pointless subscribes.
        utmHandler.handleUTMChange(gameObject.utmLocation, player);
        //need to consider do we put it in a UTM / SubUTM....probably should register our item to that topic.  makes sense.
        //and remove when we get move it out of zone.

        CheChannelFactory.write(player.getKey(), new CheMessage(Tags.GAME_OBJECT, new message.GameObject(gameObject.getMessage())));


    }

    private void objectMove(Player player, GameObject gameObject) {


        //move object in utm / subutm

    }

    private void objectHit(Player player, GameObject gameObject) {


        // publish to utm/ subutm grid a hit has occured


    }

    private void objectDestroyed(Player player, GameObject gameObject) {


        //   publish to utm / sub utm object destroyed and remove object
    }
}
