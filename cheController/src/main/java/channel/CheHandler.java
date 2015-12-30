package channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Core;
import util.Configuration;

import java.util.Map;

/**
 * Created by timmytime on 29/12/15.
 */
@ChannelHandler.Sharable
public class CheHandler extends SimpleChannelInboundHandler<Core> {

    private Configuration configuration;

    public CheHandler(Configuration configuration) {
        this.configuration = configuration;

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Core core) throws Exception {

        ctx.writeAndFlush("hello there");

        configuration.getLogger().debug("have called this "+core.getAckId());

    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        configuration.getLogger().error(cause.getMessage());
    }

}
