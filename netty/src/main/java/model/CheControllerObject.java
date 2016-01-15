package model;

import io.netty.channel.Channel;
import message.CheMessage;

import java.io.Serializable;


/**
 * Created by timmytime on 13/12/15.
 */
public class CheControllerObject implements Serializable {

    /*

     stupid idea to serialize sockets.

     therefore, either bring this back to here...or ???  same issue as before tho.  what if we want to client messages via pub / sub.

     they work as we keep hold of them

     */


    private CheMessage cheMessage;
    private Channel channel;

    public CheControllerObject(CheMessage core, Channel channel) {
        this.cheMessage = core;
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public CheMessage getCore() {
        return cheMessage;
    }
}
