package channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.CheMessage;
import model.Acknowledge;
import util.Configuration;
import util.Tags;

import java.io.IOException;

/**
 * Created by timmytime on 10/12/15.
 */
public class JsonHandler extends SimpleChannelInboundHandler<Object> {


    private final Configuration configuration;

    private model.Acknowledge ack;

    public JsonHandler(Configuration configuration) {
        this.configuration = configuration;
    }


    public void channelActive(ChannelHandlerContext ctx) {

        ack = new Acknowledge("");
        ack.state = Tags.ACCEPT;
        ack.value = Tags.ACTIVE;

        //client an acknowledge to confirm we are active.  no need to client id
        ctx.channel().writeAndFlush(ack.getMessage());
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

        configuration.getLogger().debug(new CheMessage(msg.toString()));
        ctx.fireChannelRead(new CheMessage(msg.toString()));

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        configuration.getLogger().error("error of sorts");

        ack.state = Tags.ERROR;
        ack.value = cause.toString();

        ctx.channel().writeAndFlush(ack.getMessage());

        configuration.getLogger().error(cause.getMessage());
        //really need to implement a logger.  but the base of this works.
        if (cause instanceof IOException && cause.getMessage().contains("timed out")) {
            configuration.getLogger().debug("CALLED FROM JSON HANDLER - TIMEOUT");
            ctx.channel().close();
        }
    }

}
