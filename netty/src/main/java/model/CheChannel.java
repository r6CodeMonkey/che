package model;

import io.netty.channel.Channel;
import message.Acknowledge;
import message.CheMessage;
import message.HazelcastMessage;
import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;
import util.UUIDGenerator;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;

/**
 * Created by timmytime on 25/01/16.
 */
public class CheChannel {

    private final LinkedHashMap<String, JSONObject> buffer = new LinkedHashMap<>();
    private Channel channel;
    private Object lock = new Object();
    private String lastSentKey = "";


    public CheChannel(Channel channel) {
        this.channel = channel;
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void receive(Acknowledge acknowledge) throws JSONException {
        synchronized (lock) {
            buffer.remove(acknowledge.getCheAckId());
            lastSentKey = "";
            //now try and make the buffer send the reset.
            if (!buffer.isEmpty()) {
                String msg = buffer.get(buffer.keySet().iterator().next()).toString();
                writeToChannel(msg);
            }
        }
    }

    private void send(JSONObject message) throws JSONException {
        synchronized (lock) {
            buffer.put(message.getJSONObject(Tags.CHE).getJSONObject(Tags.CHE_ACKNOWLEDGE).getString(Tags.CHE_ACK_ID), message);
            String nextKey = buffer.keySet().iterator().next();
            if (!lastSentKey.equals(nextKey)) {
                lastSentKey = nextKey;
                writeToChannel(buffer.get(lastSentKey).toString());
            }
        }

    }

    private void writeToChannel(String message) {

        if (channel != null) {
            if (channel.isActive()) {
                channel.writeAndFlush(message);
            }
        }
    }

    private JSONObject setAcknowledge(CheMessage cheMessage) throws JSONException, NoSuchAlgorithmException {

        message.Acknowledge acknowledge = new Acknowledge(true);
        acknowledge.create();
        acknowledge.setKey(UUIDGenerator.generate());
        acknowledge.setState(Tags.CHE_ACK_ID);
        acknowledge.setValue(Tags.ACCEPT);

        cheMessage.setMessage(Tags.CHE_ACKNOWLEDGE, acknowledge.getContents());

        return cheMessage;

    }


    public void write(CheMessage cheMessage) throws JSONException, NoSuchAlgorithmException {
        send(setAcknowledge(cheMessage));
    }

    public void write(HazelcastMessage cheMessage) throws JSONException, NoSuchAlgorithmException {

        if (cheMessage.isSendToSelf() || (!cheMessage.getRemoteAddress().equals(channel.remoteAddress().toString()))) {
            send(setAcknowledge(new CheMessage(new JSONObject().put(Tags.CHE, cheMessage.getCheObject()).toString())));
        }
    }
}
