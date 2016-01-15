package controller.handler;

import controller.handler.generic.AllianceHandler;
import controller.handler.generic.GameObjectHandler;
import controller.handler.generic.MissileHandler;
import core.HazelcastManagerInterface;
import message.CheMessage;
import model.Alliance;
import model.Player;
import util.Configuration;
import util.Response;
import util.Tags;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by timmytime on 31/12/15.
 */
public class GenericHandler {

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;

    //generic handlers
    private final AllianceHandler allianceHandler;
    private final GameObjectHandler gameObjectHandler;
    private final MissileHandler missileHandler;

    public GenericHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;

        allianceHandler = new AllianceHandler(hazelcastManagerInterface, configuration);
        gameObjectHandler = new GameObjectHandler(hazelcastManagerInterface, configuration);
        missileHandler = new MissileHandler(hazelcastManagerInterface, configuration);

    }

    public void handle(Player player, CheMessage cheMessage) throws RemoteException, NoSuchAlgorithmException {

        switch (cheMessage.getType()) {
            case Tags.ALLIANCE_CREATE:
                allianceHandler.allianceCreate(player, new Alliance(cheMessage.getMessage(Tags.ALLIANCE)));
                break;
            case Tags.ALLIANCE_INVITE:
                allianceHandler.allianceInvite(player, genericModel);
                break;
            case Tags.ALLIANCE_JOIN:
                allianceHandler.allianceJoin(player, genericModel);
                break;
            case Tags.ALLIANCE_LEAVE:
                allianceHandler.allianceLeave(player, genericModel);
                break;
            case Tags.ALLIANCE_POST:
                allianceHandler.alliancePost(player, genericModel);
                break;
            case Tags.GAME_OBJECT_ADD:
                gameObjectHandler.objectAdd(player, genericModel);
                break;
            case Tags.GAME_OBJECT_DESTROYED:
                gameObjectHandler.objectDestroyed(player, genericModel);
                break;
            case Tags.GAME_OBJECT_HIT:
                gameObjectHandler.objectHit(player, genericModel);
                break;
            case Tags.GAME_OBJECT_MOVE:
                gameObjectHandler.objectMove(player, genericModel);
                break;
            case Tags.MISSILE_CANCEL:
                missileHandler.missileCancel(player, genericModel);
                break;
            case Tags.MISSILE_FIRE:
                missileHandler.missileFire(player, genericModel);
                break;
            case Tags.MISSILE_TARGET:
                missileHandler.missileTarget(player, genericModel);
                break;
            default:
                configuration.getChannelMapController().getChannel(player.uid).writeAndFlush(Response.UNKNOWN_GENERIC_TYPE);
                return;
        }

    }

}
