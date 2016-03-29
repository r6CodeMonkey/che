package channel;

import controller.CheController;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.CheMessage;
import util.Configuration;

/**
 * Created by timmytime on 25/01/16.
 */
@ChannelHandler.Sharable
public class CheControllerHandler extends SimpleChannelInboundHandler<CheMessage> {

    private final Configuration configuration;
    private final CheController cheController;

    public CheControllerHandler(Configuration configuration) throws Exception {
        this.configuration = configuration;
        cheController = new CheController(configuration);

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, CheMessage cheMessage) throws Exception {


        new Thread(() -> {
            try {
                cheController.receive(cheMessage);
            } catch (Exception e) {
                configuration.getLogger().error("failed to call che controller " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        configuration.getLogger().error(cause.getMessage());
    }

}
