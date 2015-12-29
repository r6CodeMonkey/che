package channel;

import factory.MessageFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import model.CheControllerObject;
import model.Core;
import socket.CheControllerSocket;
import util.Configuration;
import util.Tags;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by timmytime on 12/12/15.
 */
public class CheHandler extends SimpleChannelInboundHandler<Core> {

    private Configuration configuration;
    private Core core;

    public CheHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Core msg) throws Exception {

        this.core = msg;
        //we can send the object to the cheController netty server.
       // CheControllerObject cheControllerObject = new CheControllerObject(core, ctx.channel());

        Socket socket = new Socket("localhost", 8086);

        CheControllerSocket cheControllerSocket = new CheControllerSocket(ctx.channel(), new DataInputStream(socket.getInputStream()));

        configuration.getLogger().debug("bind channel");

        ctx.channel().writeAndFlush("testing hello");

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
