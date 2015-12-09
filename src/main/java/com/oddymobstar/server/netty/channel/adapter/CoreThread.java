package com.oddymobstar.server.netty.channel.adapter;


import com.oddymobstar.server.netty.channel.handler.ChannelHandler;
import com.oddymobstar.server.netty.message.in.CoreMessage;
import com.oddymobstar.server.netty.message.in.InGridMessage;
import com.oddymobstar.server.netty.model.Player;
import com.oddymobstar.server.netty.store.HazelcastManager;
import com.oddymobstar.server.netty.util.Configuration;
import com.oddymobstar.server.netty.util.StoreKey;
import io.netty.channel.ChannelHandlerContext;

import java.security.NoSuchAlgorithmException;

/**
 * Created by timmytime on 24/04/15.
 */
public class CoreThread extends Thread {

    private Configuration configuration;
    private HazelcastManager hazelcastManager;
    private Player player;
    private CoreMessage coreMessage;
    private ChannelHandlerContext ctx;

    public CoreThread(Configuration configuration, HazelcastManager hazelcastManager, ChannelHandlerContext ctx, CoreMessage coreMessage) {

        this.configuration = configuration;
        this.hazelcastManager = hazelcastManager;
        this.ctx = ctx;
        this.player = coreMessage.getPlayer();
        this.coreMessage = coreMessage;

    }

    public void run() {

        if (player == null) {
            configuration.getLogger().debug("we have added a player");
            try {
                coreMessage.setUniqueIdentifier(configuration.getUuidGenerator().generatePlayerKey());
            } catch (NoSuchAlgorithmException nsae) {

            }
            player = new Player(coreMessage);

            hazelcastManager.addPlayer(player, ctx.channel());
        } else {
            ChannelHandler channel = hazelcastManager.getPlayerChannel(player);
            channel.setChannel(ctx.channel());

            player.updateLocation(coreMessage);

            if (channel == null || !channel.isChannelOpen()) {
                //update the channel with channel...unless its fucked...
                configuration.getLogger().debug("we have added a new socket channel - IMPORTANT SOMETHING WRONG re IsChannelOpen");
                player.setSocketAddress(ctx.channel().remoteAddress());
                hazelcastManager.setPlayerChannel(player, ctx.channel());

                for (StoreKey alliance : player.getAllianceKeys().values()) {
                    String regId = hazelcastManager.getAllianceStore().subscribe(alliance.getStoreID(), hazelcastManager.getPlayerChannel(player));
                    player.getAllianceKey(alliance.getStoreID()).setRegID(regId);
                }


            }
            hazelcastManager.updatePlayer(player);
        }
        //its quicker to keep the player reference in message up pipeline.
        coreMessage.setPlayer(player);

        ctx.fireChannelRead(new InGridMessage(coreMessage, configuration.getUtmConvert()));


    }
}
