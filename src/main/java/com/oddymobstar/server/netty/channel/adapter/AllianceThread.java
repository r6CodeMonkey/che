package com.oddymobstar.server.netty.channel.adapter;

import com.oddymobstar.server.netty.channel.handler.ChannelHandler;
import com.oddymobstar.server.netty.message.in.InAllianceMessage;
import com.oddymobstar.server.netty.message.out.Acknowledge;
import com.oddymobstar.server.netty.message.out.OutAllianceMessage;
import com.oddymobstar.server.netty.model.Alliance;
import com.oddymobstar.server.netty.model.Player;
import com.oddymobstar.server.netty.store.HazelcastManager;
import com.oddymobstar.server.netty.util.Configuration;
import com.oddymobstar.server.netty.util.StoreKey;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

/**
 * Created by timmytime on 24/04/15.
 */
public class AllianceThread extends Thread {

    private Configuration configuration;
    private HazelcastManager hazelcastManager;
    private ChannelHandlerContext ctx;
    private Player player;
    private Alliance alliance;
    private InAllianceMessage inAllianceMessage;


    public AllianceThread(Configuration configuration, HazelcastManager hazelcastManager, ChannelHandlerContext ctx, Alliance alliance, InAllianceMessage inAllianceMessage) {

        this.configuration = configuration;
        this.hazelcastManager = hazelcastManager;
        this.ctx = ctx;
        this.player = inAllianceMessage.getGridMessage().getCoreMessage().getPlayer();
        this.alliance = alliance;
        this.inAllianceMessage = inAllianceMessage;
    }

    public void run() {


        boolean genUUID = false;
        boolean recreateAttempt = false;


        ChannelHandler channel = hazelcastManager.getPlayerChannel(player);

        try {


            if (alliance == null) {
                //set the unique key
                String aid;
                try {
                    aid = configuration.getUuidGenerator().generateAllianceKey();
                    genUUID = true;
                    //ctx.channel().writeAndFlush("id gen is "+aid);
                    inAllianceMessage.setAid(aid);
                    alliance = new Alliance(inAllianceMessage.getAid());

                    //its always a join.  well create and join
                    hazelcastManager.getAllianceStore().getTopic(alliance.getAllianceKey().getStoreID());
                    StoreKey playerAllianceKey
                            = new StoreKey(alliance.getAllianceKey().getStoreID(),
                            hazelcastManager.getAllianceStore().subscribe(alliance.getAllianceKey().getStoreID(),
                                    channel));
                    player.addAllianceKey(playerAllianceKey);
                    alliance.addPlayer(player);
                    hazelcastManager.addAlliance(alliance);
                    hazelcastManager.updatePlayer(player);


                } catch (NoSuchAlgorithmException nsae) {
                    Acknowledge ack = new Acknowledge(inAllianceMessage.getGridMessage().getCoreMessage().getAckId(), Acknowledge.ERROR, player.getPlayerKey().getStoreID(), "", "algorithm generator failed");
                    ctx.channel().writeAndFlush(ack.getMessage().toString());
                }

            } else {
                //technically we control the alliance keys.  therefore need key generator....
                //in order to join a member must confirm they can join.
                //so need to think about this some more now.  it must be a private alliance..16.4.15 am moving away from this
                if (!inAllianceMessage.getType().equals(InAllianceMessage.ALLIANCE_CREATE)) {

                    switch (inAllianceMessage.getType()) {
                        //we are sending a message.  we can ignore disconnects
                        case InAllianceMessage.ALLIANCE_JOIN:
                            StoreKey playerAllianceKey
                                    = new StoreKey(alliance.getAllianceKey().getStoreID(),
                                    hazelcastManager.getAllianceStore().subscribe(alliance.getAllianceKey().getStoreID(),
                                            channel));
                            player.addAllianceKey(playerAllianceKey);
                            alliance.addPlayer(player);
                            hazelcastManager.addAlliance(alliance);
                            hazelcastManager.updatePlayer(player);

                            try {
                                hazelcastManager.getAllianceStore().getTopic(alliance.getAllianceKey().getStoreID()).publish(new OutAllianceMessage(inAllianceMessage, player.getPlayerKey().getStoreID(), channel.getChannel().remoteAddress().toString(), "Player has joined").getMessage().toString());

                                for (StoreKey key : alliance.getPlayerKeys().values()) {

                                    if (key.getStoreID() != player.getPlayerKey().getStoreID()) {

                                        Player alliancePlayer = hazelcastManager.getPlayer(new Player(key.getStoreID()));

                                        inAllianceMessage.getGridMessage().getCoreMessage().setLongitude(alliancePlayer.getLongitude());
                                        inAllianceMessage.getGridMessage().getCoreMessage().setLatitude(alliancePlayer.getLatitude());
                                        inAllianceMessage.getGridMessage().setSubUtm(alliancePlayer.getCurrentSubUTM());
                                        inAllianceMessage.getGridMessage().setUtm(alliancePlayer.getCurrentUTM());

                                        ctx.write(new OutAllianceMessage(inAllianceMessage, key.getStoreID(), channel.getChannel().remoteAddress().toString(), "Player in Alliance").getMessage().toString());
                                    }
                                }

                            } catch (JSONException jse) {

                            }
                            break;
                        case InAllianceMessage.ALLIANCE_LEAVE:
                            hazelcastManager.getAllianceStore().removeFromTopic(alliance.getAllianceKey().getStoreID(),
                                    player.getAllianceKey(alliance.getAllianceKey().getStoreID()).getRegID());
                            player.removeAllianceKey(alliance.getAllianceKey());
                            alliance.removePlayer(player);
                            hazelcastManager.updatePlayer(player);
                            hazelcastManager.addAlliance(alliance);
                            try {
                                hazelcastManager.getAllianceStore().getTopic(alliance.getAllianceKey().getStoreID()).publish(new OutAllianceMessage(inAllianceMessage, player.getPlayerKey().getStoreID(), channel.getChannel().remoteAddress().toString(), "Player has left").getMessage().toString());
                            } catch (JSONException jse) {

                            }
                            break;
                        case InAllianceMessage.ALLIANCE_PUBLISH:
                            switch (inAllianceMessage.getStatus()) {
                                case InAllianceMessage.ALLIANCE_GLOBAL:
                                    try {
                                        hazelcastManager.getAllianceStore().getTopic(alliance.getAllianceKey().getStoreID()).publish(new OutAllianceMessage(inAllianceMessage, player.getPlayerKey().getStoreID(), channel.getChannel().remoteAddress().toString(), inAllianceMessage.getMsg()).getMessage().toString());
                                    } catch (JSONException jse) {

                                    }
                                    break;
                            /*    case InAllianceMessage.ALLIANCE_UTM:
                                    //so rather than send it via the alliance, we send it via the UTM
                                    hazelcastManager.publishAllianceUTM(alliance, inAllianceMessage, player);
                                    break;
                                case InAllianceMessage.ALLIANCE_SUBUTM:
                                    hazelcastManager.publishAllianceSubUTM(alliance, inAllianceMessage, player);
                                    break; */
                            }
                            break;
                    }
                } else {  //could be a hack attempt
                    recreateAttempt = true;
                    Acknowledge ack = new Acknowledge(inAllianceMessage.getGridMessage().getCoreMessage().getAckId(), Acknowledge.ERROR, player.getPlayerKey().getStoreID(), "", "alliance can not be created again");
                    ctx.channel().writeAndFlush(ack.getMessage().toString());
                }

            }
            //we do not send out the id, or acknowledge success.
            if (!recreateAttempt) {
                hazelcastManager.updatePlayerChannel(player, channel);
                Acknowledge ack = new Acknowledge(inAllianceMessage.getGridMessage().getCoreMessage().getAckId(), Acknowledge.ACCEPT, player.getPlayerKey().getStoreID(), alliance.getAllianceKey().getStoreID(), genUUID ? "UUID" : "");
                ctx.channel().writeAndFlush(ack.getMessage().toString());
            }

        } catch (Exception cause) {

            Acknowledge ack = null;
            try {
                String uid = "";
                String oid = "";
                //player is valid but...best check.
                if (player != null) {
                    if (player.getPlayerKey() != null) {
                        uid = player.getPlayerKey().getStoreID();
                    }
                }

                if (alliance != null) {
                    if (alliance.getAllianceKey() != null) {
                        uid = alliance.getAllianceKey().getStoreID();
                    }
                }

                ack = new Acknowledge(inAllianceMessage.getGridMessage().getCoreMessage().getAckId(), Acknowledge.ERROR, uid, oid, cause.toString());
                ctx.channel().writeAndFlush(ack.getMessage().toString());
            } catch (JSONException e) {
                configuration.getLogger().error(e.getStackTrace());
            }

            configuration.getLogger().error(cause.getStackTrace());

        }

    }

}
