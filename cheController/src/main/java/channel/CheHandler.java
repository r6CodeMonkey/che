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

    /*
      need to implement our callback class to pass through to controller...it basically stops us sending to ourselves if we actually sent message.

      simples.  need a message too which have not implemented yet.  see schema object.  need to build our return messages.  yawn.

      upside: this is in a good state.  note it failed to reconnect to netty, if you take this down inbetween.....to review.
     */


    private final Configuration configuration;
    private final CheController cheController;

    public CheHandler(Configuration configuration) throws Exception {
        this.configuration = configuration;
        cheController = new CheController(configuration);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Core core) throws Exception {
        configuration.getChannelMapController().addChannel(core.getUser().getUid(), ctx.channel());

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
