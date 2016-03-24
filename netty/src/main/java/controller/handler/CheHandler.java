package controller.handler;

import controller.handler.che.AllianceHandler;
import controller.handler.che.GameObjectHandler;
import core.HazelcastManagerInterface;
import message.CheMessage;
import model.Alliance;
import model.GameObject;
import model.Missile;
import model.Player;
import org.json.JSONException;
import server.GameEngineInterface;
import util.Configuration;
import util.Tags;

import javax.xml.bind.JAXBException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by timmytime on 31/12/15.
 */
public class CheHandler {

    private final Configuration configuration;

    //che handlers
    private final AllianceHandler allianceHandler;
    private final GameObjectHandler gameObjectHandler;


    public CheHandler(HazelcastManagerInterface hazelcastManagerInterface, GameEngineInterface gameEngineInterface,  Configuration configuration) {
        this.configuration = configuration;

        allianceHandler = new AllianceHandler(hazelcastManagerInterface, configuration);
        gameObjectHandler = new GameObjectHandler(hazelcastManagerInterface,gameEngineInterface, configuration);

    }


    public void handle(Player player, CheMessage cheMessage) throws RemoteException, NoSuchAlgorithmException, JSONException, JAXBException {

        configuration.getLogger().debug("che handler");

        if (cheMessage.containsMessage(Tags.ALLIANCE)) {
            configuration.getLogger().debug("have alliance");
            allianceHandler.handle(player, new Alliance((message.Alliance) cheMessage.getMessage(Tags.ALLIANCE)));
        }


        if (cheMessage.containsMessage(Tags.GAME_OBJECT)) {
            configuration.getLogger().debug("have game object " + cheMessage.toString());
            gameObjectHandler.handle(player, new GameObject((message.GameObject) cheMessage.getMessage(Tags.GAME_OBJECT)));

        }


    }

}
