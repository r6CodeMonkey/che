package controller.handler.generic;

import controller.CheController;
import core.HazelcastManagerInterface;
import model.Alliance;
import model.Player;
import util.Configuration;
import util.Tags;

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

    public void handle(Player player, Alliance alliance) throws RemoteException, NoSuchAlgorithmException {


        switch (alliance.state) {
            case Tags.ALLIANCE_CREATE:
                allianceCreate(player, alliance);
                break;
            case Tags.ALLIANCE_POST:
                alliancePost(player, alliance);
                break;
            case Tags.ALLIANCE_JOIN:
                allianceJoin(player, alliance);
                break;
            case Tags.ALLIANCE_INVITE:
                allianceInvite(player, alliance);
                break;
            case Tags.ALLIANCE_LEAVE:
                allianceLeave(player, alliance);
                break;
        }
    }

    private void allianceCreate(Player player, Alliance alliance) throws RemoteException, NoSuchAlgorithmException {

        alliance.setKey(configuration.getUuidGenerator().generateKey("alliance " + alliance.name));

        alliance.getMembers().add(player);
        player.getAlliances().add(alliance);

        hazelcastManagerInterface.put(CheController.ALLIANCE_MAP, alliance.getKey(), alliance);

        player.getTopicSubscriptions().addSubscription(alliance.getKey(), hazelcastManagerInterface.subscribe(alliance.getKey(), player.getKey()));
        alliance.value = Tags.SUCCESS;

        configuration.getChannelMapController().getChannel(player.getKey()).writeAndFlush(alliance.getMessage());

    }

    private void allianceInvite(Player player, Alliance alliance) throws RemoteException, NoSuchAlgorithmException {


        //   the bt one is local, this is to publish to other people sends the alliance key to person.....

    }

    private void allianceJoin(Player player, Alliance alliance) throws RemoteException, NoSuchAlgorithmException {

        //    add a player to alliance and register to topic


    }

    private void allianceLeave(Player player, Alliance alliance) throws RemoteException, NoSuchAlgorithmException {


        //      remove player from alliance and remove from topic

    }

    private void alliancePost(Player player, Alliance alliance) throws RemoteException, NoSuchAlgorithmException {

        //     post to alliance channel

    }

}
