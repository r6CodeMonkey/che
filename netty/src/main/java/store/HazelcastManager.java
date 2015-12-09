package store;


import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import channel.handler.ChannelHandler;
import model.Alliance;
import model.Package;
import model.Player;
import util.StoreKey;
import io.netty.channel.Channel;

/**
 * Created by timmytime on 09/02/15.
 */
public class HazelcastManager {

    /*
    1: can not cluster with netty. Can cluster but need web logic esque server.  Can use multiple nodes in interim
     */

    private GridStore gridStore;
    private AllianceStore allianceStore;
    private PackageStore packageStore;

    private ChannelStore channelStore;

    private HazelcastInstance maps;


    private static final String PLAYERS = "PLAYERS";
    private static final String ALLIANCES = "ALLIANCES";
    private static final String PACKAGES = "PACKAGES";


    public HazelcastManager() {
        maps = Hazelcast.newHazelcastInstance();
        channelStore = new ChannelStore();
        gridStore = new GridStore();
        allianceStore = new AllianceStore();
        packageStore = new PackageStore();
    }

    public GridStore getGridStore() {
        return gridStore;
    }

    public AllianceStore getAllianceStore() {
        return allianceStore;
    }

    public PackageStore getPackageStore() {
        return packageStore;
    }


    public void addPlayer(Player player, Channel channel) {
        player.setSocketAddress(channel.remoteAddress());
        maps.getMap(PLAYERS).put(player.getPlayerKey().getStoreID(), player);
        channelStore.addChannel(player.getPlayerKey().getStoreID(), channel);
    }

    public void updatePlayer(Player player) {
        maps.getMap(PLAYERS).put(player.getPlayerKey().getStoreID(), player);
    }


    public void addAlliance(Alliance alliance) {
        maps.getMap(ALLIANCES).put(alliance.getAllianceKey().getStoreID(), alliance);
    }


    public void addPackage(Package pack) {
        maps.getMap(PACKAGES).put("", pack);
    }


    public Player getPlayer(Player player) {
        return (Player) maps.getMap(PLAYERS).get(player.getPlayerKey().getStoreID());
    }

    public ChannelHandler getPlayerChannel(Player player) {
        return channelStore.getChannel(player.getPlayerKey().getStoreID());
    }

    public void updatePlayerChannel(Player player, ChannelHandler channel) {
        channelStore.updateChannel(player.getPlayerKey().getStoreID(), channel);
    }

    public void setPlayerChannel(Player player, Channel channel) {
        channelStore.addChannel(player.getPlayerKey().getStoreID(), channel);
    }


    public model.Package getPackageMap(String id) {
        return (Package) maps.getMap(PACKAGES).get(id);
    }

    public Alliance getAllianceMap(String id) {
        return (Alliance) maps.getMap(ALLIANCES).get(id);
    }

    public void removePlayer(Player player) {
        maps.getMap(PLAYERS).remove(player.getPlayerKey().getStoreID());
        channelStore.removeChannel(player.getPlayerKey().getStoreID());
    }

    public void removePackage(String id) {
        maps.getMap(PACKAGES).remove(id);
    }

    public void removeAlliance(String id) {
        maps.getMap(ALLIANCES).remove(id);
    }


    //grid manager wrappers
    public void moveGrids(Player player) {

        //we also need to control the UTM channels

        String oldId = player.getPreviousUTM().getUtm() + player.getPreviousSubUTM().getUtm();
        String newId = player.getCurrentUTM().getUtm() + player.getCurrentSubUTM().getUtm();

        ChannelHandler playerChannel = getPlayerChannel(player);

        gridStore.removeSubGridItem(player.getPreviousUTM(), player.getPreviousSubUTM(), player.getPlayerKey().getStoreID());
        StoreKey gridKey = player.getGridKey(oldId);
        if (gridKey != null) {
            player.removeGridKey(gridKey);
            gridStore.removeFromTopic(oldId, gridKey.getRegID());

           /*
           was for testing is pretty pointless really other than for me...

           try {
                gridStore.getTopic(oldId).publish(new OutGridMessage(player.getPreviousUTM(), player.getPreviousSubUTM(), playerChannel.getChannel().remoteAddress().toString(), "Goodbye user " + player.getPlayerKey().getStoreID() + " to grid " + oldId).getMessage().toString());

            } catch (JSONException jse) {
                jse.printStackTrace();

            } */

        }
        //and utm grid.
        gridKey = player.getGridKey(player.getPreviousUTM().getUtm());
        if (gridKey != null) {
            player.removeGridKey(gridKey);
            gridStore.removeFromTopic(player.getPreviousUTM().getUtm(), gridKey.getRegID());
        }

        gridKey = new StoreKey(player.getCurrentUTM().getUtm(), gridStore.subscribe(player.getCurrentUTM().getUtm(), playerChannel));
        player.addGridKey(gridKey); //we dont bother adding UTM grids for tracking.
        gridKey = new StoreKey(newId, gridStore.subscribe(newId, playerChannel));
        player.addGridKey(gridKey);
        gridStore.addItem(player.getCurrentUTM(), player.getCurrentSubUTM(), player.getPlayerKey().getStoreID(), player);

        updatePlayer(player);
       /*
         is pretty pointless again for my benefit.

        try {

            gridStore.getTopic(newId).publish(new OutGridMessage(player.getCurrentUTM(), player.getCurrentSubUTM(), playerChannel.getChannel().remoteAddress().toString(), "Welcome user " + player.getPlayerKey().getStoreID() + " to grid " + newId).getMessage().toString());
        } catch (JSONException jse) {
            jse.printStackTrace();

        } */
    }


   /* public void publishAllianceUTM(Alliance alliance, InAllianceMessage allianceMessage, Player player) {
        try {
            gridStore.getTopic(allianceMessage.getUtm().getUtm()).publish(new OutAllianceMessage(allianceMessage,alliance.getAllianceKey().getStoreID(), player.getPlayerKey().getStoreID(), InAllianceMessage.ALLIANCE_PUBLISH, player.getSocketAddress().toString(), allianceMessage.getMsg()).getMessage().toString());
        } catch (JSONException jse) {
            //log
        }
    }

    public void publishAllianceSubUTM(Alliance alliance, InAllianceMessage allianceMessage, Player player) {
        try { //inAllianceMessage.getUtm(), inAllianceMessage.getSubUtm(), inAllianceMessage.getMsg()
            gridStore.getTopic(allianceMessage.getUtm().getUtm() + allianceMessage.getSubUtm().getUtm()).publish(new OutAllianceMessage(allianceMessage, alliance.getAllianceKey().getStoreID(), player.getPlayerKey().getStoreID(), InAllianceMessage.ALLIANCE_PUBLISH, player.getSocketAddress().toString(), allianceMessage.getMsg()).getMessage().toString());
        } catch (JSONException jse) {

        }
    }
*/

    public void stop() {
        maps.shutdown();
        packageStore.stop();
        gridStore.stop();
        allianceStore.stop();
        channelStore.stop();

    }


}
