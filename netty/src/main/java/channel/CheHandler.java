package channel;

import factory.MessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.CheControllerObject;
import model.Core;
import org.json.JSONException;
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
        //we can send the object to the cheController netty server.
        CheControllerObject cheControllerObject = new CheControllerObject(core, ctx.channel());

        ctx.channel().writeAndFlush(MessageFactory.createAcknowledge(core.getAckId(), Tags.SUCCESS, "Received"));

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
     try {
         ctx.channel().writeAndFlush(MessageFactory.createAcknowledge(core.getAckId(), Tags.ERROR, cause.toString()));
          configuration.getLogger().error(cause.getMessage());
     }catch (Exception e){
         configuration.getLogger().error(e.getMessage());
     }
    }
}
