package channel.adapter;

import message.in.*;
import message.out.Acknowledge;
import message.out.OutGridMessage;
import model.Player;
import store.HazelcastManager;
import util.Configuration;
import util.StoreKey;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONException;

/**
 * Created by timmytime on 24/04/15.
 */
public class GridThread extends Thread {

    private Configuration configuration;
    private HazelcastManager hazelcastManager;
    private Player player;
    private ChannelHandlerContext ctx;
    private InGridMessage msg;


    public GridThread(Configuration configuration, HazelcastManager hazelcastManager, ChannelHandlerContext ctx, InGridMessage msg) {

        this.configuration = configuration;
        this.hazelcastManager = hazelcastManager;
        this.ctx = ctx;
        this.player = msg.getCoreMessage().getPlayer();
        this.msg = msg;

    }

    @Override
    public void run() {


        boolean utmChange = player.hasUTMChanged(msg.getUtm());
        boolean subUtmChange = player.hasSubUTMChanged(msg.getSubUtm());


        if (utmChange || subUtmChange) {
            hazelcastManager.moveGrids(player);
        }

        /*
             within in the grid...we need to send our location changes to our channels.
         */

        for (StoreKey key : player.getAllianceKeys().values()) {
            try {
                hazelcastManager.getAllianceStore().getTopic(key.getStoreID()).publish(new OutGridMessage(msg.getCoreMessage(), player.getCurrentUTM(), player.getCurrentSubUTM(), player.getPlayerKey().getStoreID(), OutGridMessage.PLAYER, ctx.channel().remoteAddress().toString(), "Player moved").getMessage().toString());
            } catch (JSONException jse) {
                configuration.getLogger().error(jse.getMessage());
            }
        }


        //we need to handle are higher level messages once the location is managed.
        try {
            switch (msg.getCoreMessage().getMessageType()) {
                case CoreMessage.ALLIANCE_TYPE:
                    ctx.fireChannelRead(new InAllianceMessage(msg));
                    break;
                case CoreMessage.PACKAGE_TYPE:
                    //we need to think about this.  really this level does the various
                    ctx.fireChannelRead(new InPackageMessage(msg));
                    break;
                case CoreMessage.IMAGE_TYPE:
                    InImageMessage inImageMessage = new InImageMessage(msg.getCoreMessage());
                    player.setImage(inImageMessage.getImage());
                    break;

                default:
                    break;

            }

            if (msg.getCoreMessage().getMessageType().equals(CoreMessage.PLAYER_TYPE)
                    ||msg.getCoreMessage().getMessageType().equals(CoreMessage.IMAGE_TYPE)) {
                //acknowledge.
                Acknowledge ack = new Acknowledge(msg.getCoreMessage().getAckId(), Acknowledge.ACCEPT, player.getPlayerKey().getStoreID(), "", player.isGenUUID() ? "UUID" : "", player.getCurrentUTM().getUtm(), player.getCurrentSubUTM().getUtm());
                ctx.channel().writeAndFlush(ack.getMessage().toString());

            }
        } catch (JSONException jse) {
            //need to move the exceptions to here at some point...
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
                ack = new Acknowledge(msg.getCoreMessage().getAckId(), Acknowledge.ERROR, uid, "", jse.toString());
                ctx.channel().writeAndFlush(ack.getMessage().toString());
            } catch (JSONException e) {
                configuration.getLogger().error(e.getStackTrace());
            }

            configuration.getLogger().error(jse.getStackTrace());

        }

    }

}
