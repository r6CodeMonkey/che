package model;

import factory.MessageFactory;
import io.netty.channel.Channel;
import message.Acknowledge;
import message.CheMessage;
import message.HazelcastMessage;
import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;
import util.UUIDGenerator;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by timmytime on 25/01/16.
 */
public class CheChannel {

    private Channel channel;

    //ideally we need a list...or something ordered.
    private List<String> bufferOrder = new ArrayList<>();
    private Map<String, JSONObject> buffer = new HashMap<>();


    public CheChannel(Channel channel) {
        this.channel = channel;
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void receive(String acknowledge) throws JSONException {

        Acknowledge ack = (Acknowledge) MessageFactory.getCheMessage(acknowledge, Tags.CHE_ACKNOWLEDGE);

        buffer.remove(ack.getCheAckId());
        bufferOrder.remove(ack.getCheAckId());
    }

    private void send(JSONObject message) throws JSONException {

        bufferOrder.add(message.getJSONObject(Tags.CHE).getJSONObject(Tags.CHE_ACKNOWLEDGE).getString(Tags.CHE_ACK_ID));
        buffer.put(message.getJSONObject(Tags.CHE).getJSONObject(Tags.CHE_ACKNOWLEDGE).getString(Tags.CHE_ACK_ID), message);

        //try and send our buffer first.
        for (String key : bufferOrder) {
            //try to send it.
            writeToChannel(buffer.get(key).toString());
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

        if (cheMessage.isSendToSelf() ||(!cheMessage.getRemoteAddress().equals(channel.remoteAddress().toString()))) {
           send(setAcknowledge(new CheMessage(new JSONObject().put(Tags.CHE, cheMessage.getCheObject()).toString())));
        }
    }
}
