package model;

import io.netty.channel.Channel;
import message.HazelcastMessage;
import org.json.JSONException;

/**
 * Created by timmytime on 25/01/16.
 */
public class CheChannel {

    private final String key;
    private Channel channel;

    //need a list of messages we are buffering

    public CheChannel(String key, Channel channel){
        this.key  = key;
        this.channel = channel;
    }

    public void updateChannel(Channel channel){
        this.channel = channel;
    }

    public Channel getChannel(){
        return channel;
    }

    public String getKey(){
        return key;
    }


    /*
     fix both methods to manage the buffers...ie decouple the channel from the message...simples.
     */

    public void write(String cheMessage){
        channel.writeAndFlush(cheMessage);
    }

    public void write(HazelcastMessage cheMessage) throws JSONException{

        if (!cheMessage.getRemoteAddress().equals(channel.remoteAddress().toString())) {
            channel.writeAndFlush(cheMessage.getCheObject().toString());
        }
    }
}
