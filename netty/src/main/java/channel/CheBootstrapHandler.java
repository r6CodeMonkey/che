package channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import message.CheMessage;
import message.Player;
import model.Acknowledge;
import util.CheBootstrap;
import util.Configuration;
import util.Tags;

import java.io.UnsupportedEncodingException;

/**
 * Created by timmytime on 24/01/16.
 */
public class CheBootstrapHandler extends SimpleChannelInboundHandler<CheMessage> {

    private final Configuration configuration;
    private CheMessage cheMessage;
    private model.Acknowledge ack;
    private CheBootstrap cheBootstrap;

    public CheBootstrapHandler(Configuration configuration) throws InterruptedException {
        this.configuration = configuration;
        cheBootstrap = new CheBootstrap(configuration);
  /*      new Thread(() -> {
            try {
                cheBootstrap = new CheBootstrap(configuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start(); */
    }





    @Override
    protected void messageReceived(ChannelHandlerContext ctx, CheMessage msg) throws Exception {

        this.cheMessage = msg;

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

        configuration.getLogger().debug("pre channel future?");

      //  if(channelFuture.isSuccess()) {
        cheBootstrap.write(cheMessage);
      //  }

        configuration.getLogger().debug("post channel future?");

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
