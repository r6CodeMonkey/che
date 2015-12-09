package channel.handler;

import channel.adapter.AllianceThread;
import message.in.InAllianceMessage;
import message.out.Acknowledge;
import model.Alliance;
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
public class AllianceMessageHandler extends SimpleChannelInboundHandler<InAllianceMessage> {

    private Configuration configuration;
    private HazelcastManager hazelcastManager;
    private Player player;
    private Alliance alliance;
    private InAllianceMessage inAllianceMessage;

    public AllianceMessageHandler(Configuration configuration, HazelcastManager hazelcastManager) {
        this.configuration = configuration;
        this.hazelcastManager = hazelcastManager;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, InAllianceMessage msg) throws Exception {


        inAllianceMessage = msg;

        player = msg.getGridMessage().getCoreMessage().getPlayer();
        //does the alliance exist?
        alliance = hazelcastManager.getAllianceMap(inAllianceMessage.getAid());

        new AllianceThread(configuration, hazelcastManager, ctx, alliance, inAllianceMessage).start();


    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        Acknowledge ack = null;
        try {
            String uid = "";
            String oid = "";
            //player is valid but...best check.
            if (player != null) {
                if (player.getPlayerKey() != null) {
                    uid = player.getPlayerKey().getStoreID();
                }
            }

            if (alliance != null) {
                if (alliance.getAllianceKey() != null) {
                    uid = alliance.getAllianceKey().getStoreID();
                }
            }

            ack = new Acknowledge(inAllianceMessage.getGridMessage().getCoreMessage().getAckId(), Acknowledge.ERROR, uid, oid, cause.toString());
            ctx.channel().writeAndFlush(ack.getMessage().toString());
        } catch (JSONException e) {
            configuration.getLogger().error(e.getStackTrace());
        }

        configuration.getLogger().error(cause.getStackTrace());


    }

}
