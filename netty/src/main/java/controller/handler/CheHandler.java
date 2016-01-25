package controller.handler;

import controller.handler.che.AllianceHandler;
import controller.handler.che.GameObjectHandler;
import controller.handler.che.MissileHandler;
import core.HazelcastManagerInterface;
import message.CheMessage;
import model.Alliance;
import model.GameObject;
import model.Missile;
import model.Player;
import org.json.JSONException;
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

    //che handlers
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

    public void handle(Player player, CheMessage cheMessage) throws RemoteException, NoSuchAlgorithmException, JSONException {


        if (!cheMessage.getJSONObject(Tags.CHE).isNull(Tags.ALLIANCE)) {
            allianceHandler.handle(player, new Alliance((message.Alliance) cheMessage.getMessage(Tags.ALLIANCE)));
        }

        if (!cheMessage.getJSONObject(Tags.CHE).isNull(Tags.MISSILE)) {
            missileHandler.handle(player, new Missile((message.Missile) cheMessage.getMessage(Tags.MISSILE)));
        }

        if (!cheMessage.getJSONObject(Tags.CHE).isNull(Tags.GAME_OBJECT)) {
            gameObjectHandler.handle(player, new GameObject((message.GameObject) cheMessage.getMessage(Tags.GAME_OBJECT)));

        }


    }

}
