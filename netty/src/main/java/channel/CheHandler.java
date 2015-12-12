package channel;

import factory.MessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Core;
import util.Configuration;
import util.Tags;

/**
 * Created by timmytime on 12/12/15.
 */
public class CheHandler extends SimpleChannelInboundHandler<Core> {

    private Configuration configuration;
    private Core core;

    public CheHandler(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Core msg) throws Exception {

        this.core = msg;

        //we now need to connect to the CheController server and send the Core msg object, and the users channel.

        ctx.channel().writeAndFlush(MessageFactory.createAcknowledge(core.getAckId(), Tags.SUCCESS, "Received"));

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().writeAndFlush(MessageFactory.createAcknowledge(core.getAckId(), Tags.ERROR, cause.toString()));
        configuration.getLogger().error(cause.getMessage());
    }
}
