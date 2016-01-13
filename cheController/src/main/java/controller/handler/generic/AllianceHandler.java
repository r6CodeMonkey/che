package controller.handler.generic;

import controller.CheController;
import core.HazelcastManagerInterface;
import model.Alliance;
import model.client.generic.GenericModel;
import model.server.Player;
import util.Configuration;

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

    public void allianceCreate(Player player, GenericModel genericModel) throws RemoteException, NoSuchAlgorithmException {
        /*
         create ourselves an alliance and register to topic...and then send the key back
         */
        Alliance alliance = new Alliance(genericModel.getValue());
        alliance.addMember(player.uid);
        String allianceKey = configuration.getUuidGenerator().generateKey("alliance " + genericModel.getValue());
        hazelcastManagerInterface.put(CheController.ALLIANCE_MAP, allianceKey, alliance);

        //need to do some subscription, and then send our message back to user...needs a proper format though.
        //define a type.
        configuration.getChannelMapController().getChannel(player.uid).writeAndFlush("");

    }

    public void allianceInvite(Player player, GenericModel genericModel) {

        /*
          the bt one is local, this is to publish to other people sends the alliance key to person.....
         */
    }

    public void allianceJoin(Player player, GenericModel genericModel) {
        /*
          add a player to alliance and register to topic
         */

    }

    public void allianceLeave(Player player, GenericModel genericModel) {

        /*
         remove player from alliance and remove from topic
         */
    }

    public void alliancePost(Player player, GenericModel genericModel) {
     /*
        post to alliance channel
      */
    }

}
