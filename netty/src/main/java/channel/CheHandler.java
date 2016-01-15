package channel;

import factory.MessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.CheMessage;
import model.Acknowledge;
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
public class CheHandler extends SimpleChannelInboundHandler<CheMessage> {

    private final List<CheMessage> pendingMessages = new ArrayList<>();
    private final Configuration configuration;
    private CheMessage cheMessage;
    private model.Acknowledge ack;
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
    protected void messageReceived(ChannelHandlerContext ctx, CheMessage cheMessage) throws Exception {

        this.cheMessage = cheMessage;

        //at this point, if the user has no id, we will create them one, even if the server is down elsewhere.
        if (cheMessage.getMessage(Tags.PLAYER).getKey().isEmpty()) {
            configuration.getLogger().debug("new user created " + ctx.channel().remoteAddress().toString());
            cheMessage.getMessage(Tags.PLAYER).setKey(configuration.getUuidGenerator().generatePlayerKey());

            ack = new Acknowledge(cheMessage.getKey());
            ack.state = Tags.UUID;
            ack.state = cheMessage.getMessage(Tags.PLAYER).getKey();

            ctx.channel().writeAndFlush(MessageFactory.getMessage(ack.getMessage()));
        }

        if (socket == null || socket.isClosed()) {
            socketAvailable = initSocket();
        }

        if (socketAvailable) {

            if (cheControllerSocket == null || !cheControllerSocket.getChannel().isOpen()) {
                cheControllerSocket = new CheControllerSocket(configuration, ctx.channel(), socket);
            }

            configuration.getLogger().debug(cheMessage.toString());
            cheControllerSocket.write(cheMessage);

            for (CheMessage pending : pendingMessages) {
                cheControllerSocket.write(pending);
            }

        } else {
            pendingMessages.add(cheMessage);
        }


        ack.state = Tags.SUCCESS;
        ack.value = "Received";  //needs to go in tags...

        ctx.channel().writeAndFlush(MessageFactory.getMessage(ack.getMessage()));

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            ack.state = Tags.ERROR;
            ack.value = cause.toString();

            ctx.channel().writeAndFlush(MessageFactory.getMessage(ack.getMessage()));
            configuration.getLogger().error(cause.getMessage());
        } catch (Exception e) {
            configuration.getLogger().error(e.getMessage());
        }
    }
}
