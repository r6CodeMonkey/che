package com.oddymobstar.server.netty.channel.handler;

import com.hazelcast.core.MessageListener;
import com.oddymobstar.server.netty.message.out.Message;
import com.oddymobstar.server.netty.message.out.OutAllianceMessage;
import com.oddymobstar.server.netty.message.out.OutGridMessage;
import io.netty.channel.Channel;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timmytime on 18/02/15.
 */
public class ChannelHandler implements MessageListener {

    private Channel channel;


    public ChannelHandler(Channel channel) {
        this.channel = channel;
    }


    public Channel getChannel() {
        return channel;
    }

    public boolean isChannelOpen() {
        return channel.isOpen();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void onMessage(com.hazelcast.core.Message message) {
        try {
            JSONObject msg = new JSONObject(message.getMessageObject().toString()).getJSONObject(Message.CORE);
            JSONObject send = new JSONObject(message.getMessageObject().toString());


            //need to fix all this up at some point.  is for testing phase.
            System.out.println("message is " + msg.toString());


            if (!msg.isNull(OutAllianceMessage.ALLIANCE)) {
                //it is always set
                if (!msg.getJSONObject(OutAllianceMessage.ALLIANCE).getString(Message.ADDRESS).equals(channel.remoteAddress().toString())) {
                    channel.writeAndFlush(send.toString());
                }
            } else if (!msg.isNull(OutGridMessage.GRID)) {
                if (!msg.getJSONObject(OutGridMessage.GRID).getString(Message.ADDRESS).equals(channel.remoteAddress().toString())) {
                    channel.writeAndFlush(send.toString());
                }
            } else {
                //its probably just an acknowledge so send it
                channel.writeAndFlush(send.toString());

            }

        } catch (JSONException jse) {
            //we have an issue.  just log it and give up we dont want a loop.
            jse.printStackTrace();
        }

    }
}
