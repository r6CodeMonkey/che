package controller.handler.che;

import core.HazelcastManagerInterface;
import model.Missile;
import model.Player;
import util.Configuration;
import util.Tags;

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

    public void handle(Player player, Missile missile) {

        switch (missile.state) {

            case Tags.MISSILE_FIRE:
                missileFire(player, missile);
                break;
            case Tags.MISSILE_TARGET:
                missileTarget(player, missile);
                break;
            case Tags.MISSILE_CANCEL:
                missileCancel(player, missile);
                break;

        }

    }



    private void missileTarget(Player player, Missile missile) {

        /*
          add target to utm / sub utm return missile key
         */

    }

    private void missileFire(Player player, Missile missile) {

        /*
         deploy missile to target / publish attack to sector
         */
    }

    private void missileCancel(Player player, Missile missile) {

        /*
          remove missile from utm / subutm
         */
    }

}
