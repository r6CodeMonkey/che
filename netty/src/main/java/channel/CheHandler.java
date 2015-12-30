package channel;

import factory.MessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Core;
import socket.CheControllerSocket;
import util.Configuration;
import util.Tags;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by timmytime on 12/12/15.
 */
public class CheHandler extends SimpleChannelInboundHandler<Core> {

    private Configuration configuration;
    private Core core;
    private Socket socket;
    private CheControllerSocket cheControllerSocket;
    private boolean socketAvailable = false;


    private final List<Core> pendingMessages = new ArrayList<>();


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

        configuration.getLogger().debug("ack id is "+msg.getAckId());


        if (socket == null || socket.isClosed()) {
            socketAvailable = initSocket();
        }

        if (socketAvailable) {

            if (cheControllerSocket == null || !cheControllerSocket.getChannel().isOpen()) {
                cheControllerSocket = new CheControllerSocket(configuration, ctx.channel(), socket);
            }

             cheControllerSocket.write(msg);

            for(Core pending : pendingMessages){
                cheControllerSocket.write(pending);
            }

        }else{
            pendingMessages.add(msg);
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
