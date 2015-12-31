package util;

import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

/**
 * Created by timmytime on 31/12/15.
 */
public class SimpleHandler implements MessageListener {

    private Message lastMessage;

    public Message getLastMessage() {
        return lastMessage;
    }

    public void onMessage(Message message) {
        //more to do here.
        lastMessage = message;

    }
}
