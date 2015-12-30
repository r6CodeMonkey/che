package channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Core;
import util.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by timmytime on 29/12/15.
 */
@ChannelHandler.Sharable
public class CheHandler extends SimpleChannelInboundHandler<Core> {

    private final Configuration configuration;

    public CheHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Core core) throws Exception {
        configuration.getChannelMapController().addChannel(core.getUser().getUid(), ctx.channel());
        ctx.writeAndFlush("hello there");
        //this can be focal point.  basically we can call a che controller in seperate thread.  simples.
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        configuration.getLogger().error(cause.getMessage());
    }

}
