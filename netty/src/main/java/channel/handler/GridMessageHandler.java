package channel.handler;

import channel.adapter.GridThread;
import message.in.InGridMessage;
import message.out.Acknowledge;
import model.Player;
import store.HazelcastManager;
import util.Configuration;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.json.JSONException;

/**
 * Created by timmytime on 17/02/15.
 */
@ChannelHandler.Sharable
public class GridMessageHandler extends SimpleChannelInboundHandler<InGridMessage> {

    private Configuration configuration;
    private HazelcastManager hazelcastManager;

    private Player player;
    private InGridMessage inGridMessage;

    public GridMessageHandler(Configuration configuration, HazelcastManager hazelcastManager) {
        this.configuration = configuration;
        this.hazelcastManager = hazelcastManager;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, InGridMessage msg) throws Exception {
        //we know this player is valid from core channel.

        inGridMessage = msg;
        this.player = msg.getCoreMessage().getPlayer();

        new GridThread(configuration, hazelcastManager, ctx, inGridMessage).start();

    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {


        Acknowledge ack = null;
        try {
            String uid = "";
            //again its a critical one at this point. we should have a UID tho..but lets not crash
            //the error stack or we do have a problem.
            if (player != null) {
                if (player.getPlayerKey() != null) {
                    uid = player.getPlayerKey().getStoreID();
                }
            }
            ack = new Acknowledge(inGridMessage.getCoreMessage().getAckId(), Acknowledge.ERROR, uid, "", cause.toString());
            ctx.channel().writeAndFlush(ack.getMessage().toString());
        } catch (JSONException e) {
            configuration.getLogger().error(e.getStackTrace());
        }

        configuration.getLogger().error(cause.getStackTrace());


    }


}
