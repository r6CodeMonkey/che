package channel.handler;

import channel.adapter.CoreThread;
import message.in.CoreMessage;
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
public class CoreMessageHandler extends SimpleChannelInboundHandler<CoreMessage> {

    private Configuration configuration;
    private HazelcastManager hazelcastManager;

    private Player player;
    private CoreMessage coreMessage;

    public CoreMessageHandler(Configuration configuration, HazelcastManager hazelcastManager) {
        this.configuration = configuration;
        this.hazelcastManager = hazelcastManager;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, CoreMessage coreMessage) throws Exception {

        this.coreMessage = coreMessage;


        player = new Player(coreMessage.getUniqueIdentifier());
        player = hazelcastManager.getPlayer(player);

        coreMessage.setPlayer(player);

        new CoreThread(configuration, hazelcastManager, ctx, coreMessage).start();

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
            ack = new Acknowledge(coreMessage.getAckId(), Acknowledge.ERROR, uid, "", cause.toString());
            ctx.channel().writeAndFlush(ack.getMessage().toString());
        } catch (JSONException e) {
            configuration.getLogger().error(e.getMessage());
        }

        configuration.getLogger().error(cause.getMessage());


    }
}
