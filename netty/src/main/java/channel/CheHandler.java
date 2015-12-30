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
import java.net.Socket;

/**
 * Created by timmytime on 12/12/15.
 */
public class CheHandler extends SimpleChannelInboundHandler<Core> {

    private Configuration configuration;
    private Core core;
    private Socket socket;
    private CheControllerSocket cheControllerSocket;

    public CheHandler(Configuration configuration) {
        this.configuration = configuration;
        initSocket();
    }

    private void initSocket() {
        try {
            socket = new Socket(configuration.getCheIP(), configuration.getChePort());
        } catch (IOException e) {
            configuration.getLogger().error("Che Port is not Available " + e.getMessage());
        }
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Core msg) throws Exception {

        this.core = msg;

        if (socket == null || socket.isClosed()) {
            initSocket();
        }

        if (cheControllerSocket == null || !cheControllerSocket.getChannel().isOpen()) {
            cheControllerSocket = new CheControllerSocket(configuration, ctx.channel(), socket);
        }


        //at some point do we want to consider netty?
        //also need to test what happens when we lose the connection / channel.  2 cases.  case 1 is above, case to need to update channel.
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("this is a test");

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
