package controller.handler.generic;

import core.HazelcastManagerInterface;
import model.client.generic.GenericModel;
import model.server.Player;
import util.Configuration;

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

    public void objectAdd(Player player, GenericModel genericModel) {

        /*
        add object to utm / sub utm grid + lat lng return key
         */


    }

    public void objectMove(Player player, GenericModel genericModel) {

        /*
         move object in utm / subutm
         */
    }

    public void objectHit(Player player, GenericModel genericModel) {

        /*
          publish to utm/ subutm grid a hit has occured
         */

    }

    public void objectDestroyed(Player player, GenericModel genericModel) {

        /*
        publish to utm / sub utm object destroyed and remove object
         */
    }
}
