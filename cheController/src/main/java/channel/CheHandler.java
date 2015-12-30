package channel;

import controller.CheController;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Core;
import util.Configuration;


/**
 * Created by timmytime on 29/12/15.
 */
@ChannelHandler.Sharable
public class CheHandler extends SimpleChannelInboundHandler<Core> {

    private final Configuration configuration;
    private final CheController cheController;

    public CheHandler(Configuration configuration) throws Exception {
        this.configuration = configuration;
        cheController = new CheController(configuration);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Core core) throws Exception {
        configuration.getChannelMapController().addChannel(core.getUser().getUid(), ctx.channel());
        //this is for testing early doors...remove soon.
        ctx.writeAndFlush("hello there");

        new Thread(() -> {
            try {
                cheController.receive(core);
            } catch (Exception e) {
                configuration.getLogger().error("failed to call che controller " + e.getMessage());
            }
        }).start();
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        configuration.getLogger().error(cause.getMessage());
    }

}
