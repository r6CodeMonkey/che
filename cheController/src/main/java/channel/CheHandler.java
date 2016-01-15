package channel;

import controller.CheController;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.CheMessage;
import util.Configuration;
import util.Tags;


/**
 * Created by timmytime on 29/12/15.
 */
@ChannelHandler.Sharable
public class CheHandler extends SimpleChannelInboundHandler<CheMessage> {


    private final Configuration configuration;
    private final CheController cheController;

    public CheHandler(Configuration configuration) throws Exception {
        this.configuration = configuration;
        cheController = new CheController(configuration);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, CheMessage cheMessage) throws Exception {
        configuration.getChannelMapController().addChannel(cheMessage.getMessage(Tags.PLAYER).getKey(), ctx.channel());

        new Thread(() -> {
            try {
                cheController.receive(cheMessage);
            } catch (Exception e) {
                configuration.getLogger().error("failed to call che controller " + e.getMessage());
            }
        }).start();
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        configuration.getLogger().error(cause.getMessage());
    }

}
