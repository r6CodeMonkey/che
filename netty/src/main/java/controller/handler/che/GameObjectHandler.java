package controller.handler.che;

import core.HazelcastManagerInterface;
import model.GameObject;
import model.Player;
import util.Configuration;
import util.Tags;

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

    public GameObjectHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
    }

    public void handle(Player player, GameObject gameObject) {

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

    private void purchaseGameObject(Player player, GameObject gameObject){
        //we add the object to db and tell user....
    }

    private void missileAdded(Player player, GameObject gameObject) {
        //we can work out what missile it is...probably call the missile handler to process.

    }

    private void missileRemoved(Player player, GameObject gameObject) {
        //remove missile from the game object...ie its been transferred elsewhere.

    }

    private void objectAdd(Player player, GameObject gameObject) {


        //add object to utm / sub utm grid + lat lng return key


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
