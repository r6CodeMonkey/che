package channel;

import factory.CheChannelFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.CheMessage;
import message.Player;
import model.Acknowledge;
import util.Configuration;
import util.Tags;


/**
 * Created by timmytime on 12/12/15.
 */
public class CheHandler extends SimpleChannelInboundHandler<CheMessage> {

    private final Configuration configuration;
    private CheMessage cheMessage;
    private model.Acknowledge ack;

    public CheHandler(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, CheMessage cheMessage) throws Exception {

        this.cheMessage = cheMessage;

        //at this point, if the user has no id, we will create them one, even if the server is down elsewhere.
        if (cheMessage.getMessage(Tags.PLAYER).getKey().isEmpty()) {
            configuration.getLogger().debug("new user created " + ctx.channel().remoteAddress().toString());
            Player player = (Player) cheMessage.getMessage(Tags.PLAYER);
            player.setKey(configuration.getUuidGenerator().generatePlayerKey());

            ack = new Acknowledge(cheMessage.getMessage(Tags.ACKNOWLEDGE).getKey());
            ack.state = Tags.UUID;
            ack.value = player.getKey();
            cheMessage.setMessage(Tags.PLAYER, player);

            ctx.channel().writeAndFlush(ack.getMessage());
        }


        ack.state = Tags.SUCCESS;
        ack.value = "Received";  //needs to go in tags...

        ctx.channel().writeAndFlush(ack.getMessage());

        if (cheMessage.containsMessage(Tags.CHE_ACKNOWLEDGE)) {
            CheChannelFactory.getCheChannel(cheMessage.getMessage(Tags.PLAYER).getKey()).receive(cheMessage.getMessage(Tags.CHE_ACKNOWLEDGE).toString());
        } else {
            //fire up pipeline...
            ctx.fireChannelRead(cheMessage);
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        try {
            ack = new Acknowledge(cheMessage.getMessage(Tags.ACKNOWLEDGE).getKey());
            ack.state = Tags.ERROR;
            ack.value = cause.toString();

            ctx.channel().writeAndFlush(ack.getMessage());
            configuration.getLogger().error(cause.getMessage());
        } catch (Exception e) {
            configuration.getLogger().error(e.getMessage());
        }
    }
}
