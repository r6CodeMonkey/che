package model;

import io.netty.channel.Channel;

import java.io.Serializable;


/**
 * Created by timmytime on 13/12/15.
 */
public class CheControllerObject implements Serializable {

    private Core core;
    private Channel channel;

    public CheControllerObject(Core core, Channel channel) {
        this.core = core;
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public Core getCore() {
        return core;
    }
}
