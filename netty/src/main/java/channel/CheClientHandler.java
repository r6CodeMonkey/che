package channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import util.Configuration;

/**
 * Created by timmytime on 24/01/16.
 */
public class CheClientHandler extends ChannelHandlerAdapter {

    private final Configuration configuration;

    public CheClientHandler(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

/*
        ByteBuf buffer = (ByteBuf) msg; // (1)

        for (int i = 0; i < buffer.capacity(); i ++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        } */

        configuration.getLogger().debug("read");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        configuration.getLogger().error("exception on client handler", cause);
        ctx.close();
    }
}
