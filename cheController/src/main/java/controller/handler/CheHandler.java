package controller.handler;

import controller.handler.generic.AllianceHandler;
import controller.handler.generic.GameObjectHandler;
import controller.handler.generic.MissileHandler;
import core.HazelcastManagerInterface;
import message.CheMessage;
import model.Player;
import util.Configuration;
import util.Tags;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by timmytime on 31/12/15.
 */
public class CheHandler {

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;

    //generic handlers
    private final AllianceHandler allianceHandler;
    private final GameObjectHandler gameObjectHandler;
    private final MissileHandler missileHandler;

    public CheHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;

        allianceHandler = new AllianceHandler(hazelcastManagerInterface, configuration);
        gameObjectHandler = new GameObjectHandler(hazelcastManagerInterface, configuration);
        missileHandler = new MissileHandler(hazelcastManagerInterface, configuration);

    }

    public void handle(Player player, CheMessage cheMessage) throws RemoteException, NoSuchAlgorithmException {


        if (!cheMessage.isNull(Tags.ALLIANCE)) {

        }

        if (!cheMessage.isNull(Tags.MISSILE)) {

        }

        if (!cheMessage.isNull(Tags.GAME_OBJECT)) {

        }

        if (!cheMessage.isNull(Tags.MISSILE)) {

        }


    }

}
