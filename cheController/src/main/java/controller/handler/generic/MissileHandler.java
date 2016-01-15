package controller.handler.generic;

import core.HazelcastManagerInterface;
import model.Missile;
import model.Player;
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

    public void missileTarget(Player player, Missile missile) {

        /*
          add target to utm / sub utm return missile key
         */

    }

    public void missileFire(Player player, Missile missile) {

        /*
         deploy missile to target / publish attack to sector
         */
    }

    public void missileCancel(Player player, Missile missile) {

        /*
          remove missile from utm / subutm
         */
    }

}
