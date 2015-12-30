package channel;

import factory.MessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Core;
import socket.CheControllerSocket;
import util.Configuration;
import util.Tags;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 12/12/15.
 */
public class CheHandler extends SimpleChannelInboundHandler<Core> {

    private final List<Core> pendingMessages = new ArrayList<>();
    private final Configuration configuration;
    private Core core;
    private Socket socket;
    private CheControllerSocket cheControllerSocket;
    private boolean socketAvailable = false;


    public CheHandler(Configuration configuration) {
        this.configuration = configuration;
        socketAvailable = initSocket();
    }


    private boolean initSocket() {
        try {
            socket = new Socket(configuration.getCheIP(), configuration.getChePort());
            return true;
        } catch (IOException e) {
            configuration.getLogger().error("Che Port is not Available " + e.getMessage());
            return false;
        }
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Core msg) throws Exception {

        this.core = msg;

        //at this point, if the user has no id, we will create them one, even if the server is down elsewhere.
        if (core.getUser().getUid().trim().isEmpty()) {
            configuration.getLogger().debug("new user created " + ctx.channel().remoteAddress().toString());
            core.updateUserID(configuration.getUuidGenerator().generatePlayerKey());
            ctx.channel().writeAndFlush(MessageFactory.createAcknowledge(core.getAckId(), Tags.UUID, core.getUser().getUid()));
        }

        if (socket == null || socket.isClosed()) {
            socketAvailable = initSocket();
        }

        if (socketAvailable) {

            if (cheControllerSocket == null || !cheControllerSocket.getChannel().isOpen()) {
                cheControllerSocket = new CheControllerSocket(configuration, ctx.channel(), socket);
            }

            cheControllerSocket.write(core);

            for (Core pending : pendingMessages) {
                cheControllerSocket.write(pending);
            }

        } else {
            pendingMessages.add(core);
        }


        ctx.channel().writeAndFlush(MessageFactory.createAcknowledge(core.getAckId(), Tags.SUCCESS, "Received"));

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            ctx.channel().writeAndFlush(MessageFactory.createAcknowledge(core.getAckId(), Tags.ERROR, cause.toString()));
            configuration.getLogger().error(cause.getMessage());
        } catch (Exception e) {
            configuration.getLogger().error(e.getMessage());
        }
    }
}
