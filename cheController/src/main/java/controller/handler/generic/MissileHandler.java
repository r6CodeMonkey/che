package controller.handler.generic;

import core.HazelcastManagerInterface;
import model.client.generic.GenericModel;
import model.server.Player;
import util.Configuration;

/**
 * Created by timmytime on 13/01/16.
 */
public class MissileHandler {

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;

    public MissileHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
    }

    public void missileTarget(Player player, GenericModel genericModel) {

        /*
          add target to utm / sub utm return missile key
         */

    }

    public void missileFire(Player player, GenericModel genericModel) {

        /*
         deploy missile to target / publish attack to sector
         */
    }

    public void missileCancel(Player player, GenericModel genericModel) {

        /*
          remove missile from utm / subutm
         */
    }

}
