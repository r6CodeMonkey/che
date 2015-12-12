package channel.handler;

import factory.MessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Acknowledge;
import model.Core;
import org.json.JSONException;
import org.json.JSONObject;
import util.Configuration;
import util.Tags;

import java.io.IOException;

/**
 * Created by timmytime on 10/12/15.
 */
public class JsonHandler extends SimpleChannelInboundHandler<Object> {


    private Configuration configuration;

    public JsonHandler(Configuration configuration) {
        this.configuration = configuration;
    }


    public void channelActive(ChannelHandlerContext ctx) {
        //send an acknowledge to confirm we are active.  no need to send id
        ctx.channel().writeAndFlush(MessageFactory.createAcknowledge("", Tags.ACCEPT, Tags.ACTIVE));
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

        Core core = (Core)MessageFactory.getMessage(MessageFactory.CORE, msg.toString());

        //does the message contain what is required.
        if (core.getAckId().trim().isEmpty()) {
            ctx.channel().writeAndFlush(MessageFactory.createAcknowledge("", Tags.ERROR, "ackid not set"));
        } else {
            /** fire it up the pipeline  need to think about this,
             *
             *  we now have a valid object.  do we need one more level, or should the message simply be handed off now to
             *  camel / controller to actually manage everything else.
             */
           // ctx.fireChannelRead(coreMessage);
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        ctx.channel().writeAndFlush(MessageFactory.createAcknowledge("", Tags.ERROR, cause.toString()));

        configuration.getLogger().error(cause.getMessage());
        //really need to implement a logger.  but the base of this works.
        if (cause instanceof IOException && cause.getMessage().contains("timed out")) {
            configuration.getLogger().debug("CALLED FROM JSON HANDLER - TIMEOUT");
            ctx.channel().close();
        }
    }

}
