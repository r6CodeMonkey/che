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

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;

    public GameObjectHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
    }

    public void handle(Player player, GameObject gameObject){

        switch(gameObject.state){
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
        }
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
