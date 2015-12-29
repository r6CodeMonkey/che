package channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.Configuration;

/**
 * Created by timmytime on 29/12/15.
 */
@ChannelHandler.Sharable
public class CheHandler extends SimpleChannelInboundHandler<String> {

    private Configuration configuration;

    public CheHandler(Configuration configuration){
        this.configuration = configuration;

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String channel) throws Exception{

        ctx.writeAndFlush("hello there");

    }



    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            configuration.getLogger().error(cause.getMessage());
    }

}
