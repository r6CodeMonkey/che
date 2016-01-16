package controller.handler.generic;

import controller.CheController;
import core.HazelcastManagerInterface;
import model.Alliance;
import model.Player;
import util.Configuration;
import util.Response;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by timmytime on 13/01/16.
 */
public class AllianceHandler {

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;

    public AllianceHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
    }

   /* public void allianceCreate(Player player, Alliance alliance) throws RemoteException, NoSuchAlgorithmException {

        Alliance alliance = new Alliance(configuration.getUuidGenerator().generateKey("alliance " + genericModel.getValue()), genericModel.getValue());
        alliance.getMembers().add(player);
        player.alliances.add(alliance);
        hazelcastManagerInterface.put(CheController.ALLIANCE_MAP, alliance.getKey(), alliance);

        player.getTopicSubscriptions().addSubscription(alliance.getKey(), hazelcastManagerInterface.subscribe(alliance.getKey(), player.getKey()));
        configuration.getChannelMapController().getChannel(player.getKey()).writeAndFlush(Response.CREATED_ALLIANCE + "need to add the key here.");

    } */
/*
    public void allianceInvite(Player player, GenericModel genericModel) {


          the bt one is local, this is to publish to other people sends the alliance key to person.....

    }

    public void allianceJoin(Player player, GenericModel genericModel) {

          add a player to alliance and register to topic


    }

    public void allianceLeave(Player player, GenericModel genericModel) {


         remove player from alliance and remove from topic

    }

    public void alliancePost(Player player, GenericModel genericModel) {

        post to alliance channel

    }
*/
}
