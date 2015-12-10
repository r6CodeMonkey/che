package channel.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.json.JSONException;
import util.Configuration;

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
        //send an acknowledge //String ackId, int type, String uid, String oid, String msg
   /*     try {
            Acknowledge ack = new Acknowledge("", Acknowledge.ACCEPT, "", "", ACTIVE);
            ctx.channel().writeAndFlush(ack.getMessage().toString());
        } catch (JSONException jse) {

        } */

    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {


     /*   CoreMessage coreMessage = new CoreMessage(msg.toString());

        //does the message contain what is required.
        if (coreMessage.getAckId().trim().isEmpty()) {
            Acknowledge ack = null;
            try {
                //send back we cant continue
                ack = new Acknowledge("", Acknowledge.ERROR, "", "", "ackid not set");
                ctx.channel().writeAndFlush(ack.getMessage().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

            //fire it up the pipeline. we should split the handlers into core - alliance - grid - package etc
            ctx.fireChannelRead(coreMessage);
        }
       */

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {


     /*   Acknowledge ack = null;
        try {
            //we cant write out details as its a json fail...
            ack = new Acknowledge("", Acknowledge.ERROR, "", "", cause.toString());
            ctx.channel().writeAndFlush(ack.getMessage().toString());
        } catch (JSONException e) {
            configuration.getLogger().error(e.getMessage());
        }


        configuration.getLogger().error(cause.getMessage());
        //really need to implement a logger.  but the base of this works.
        if (cause instanceof IOException && cause.getMessage().contains("timed out")) {
            configuration.getLogger().debug("CALLED FROM JSON HANDLER - TIMEOUT");
            ctx.channel().close();
        } */
    }

}
