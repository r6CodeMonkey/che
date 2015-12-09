package com.oddymobstar.server.netty.channel.handler;

import com.oddymobstar.server.netty.message.in.InPackageMessage;
import com.oddymobstar.server.netty.store.HazelcastManager;
import com.oddymobstar.server.netty.util.Configuration;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by timmytime on 17/02/15.
 */
@ChannelHandler.Sharable
public class PackageMessageHandler extends SimpleChannelInboundHandler<InPackageMessage> {

    private Configuration configuration;
    private HazelcastManager hazelcastManager;


    public PackageMessageHandler(Configuration configuration, HazelcastManager hazelcastManager) {
        this.configuration = configuration;
        this.hazelcastManager = hazelcastManager;
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, InPackageMessage msg) throws Exception {

        /*
        this is slightly more complicated.

        Packages have their own channels, and also will be referenced on the grid manager

        channels only close when a package is finished.  therefore all other players have knowledge
        of it if using it.


        dont go near this yet.  basically this is supposed to do a lot,

        we also need to allow people to plant devices / trackers on items and then track them

        ie esponiage style.  but user who tracks registers to item, can publish its movement to alliance

        host / stroke package is not aware.  ie we never push to package, only registered listeners



         */

    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        //really need to implement a logger.  but the base of this works.
    }

}
