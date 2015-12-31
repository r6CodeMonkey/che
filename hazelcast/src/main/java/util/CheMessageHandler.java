package util;

import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import server.CheCallbackInterface;

import java.io.Serializable;

/**
 * Created by timmytime on 31/12/15.
 */
public class CheMessageHandler implements MessageListener {

    private final String playerKey;
    private final CheCallbackInterface cheCallbackInterface;

    public CheMessageHandler(CheCallbackInterface cheCallbackInterface, String playerKey){
        this.playerKey = playerKey;
        this.cheCallbackInterface = cheCallbackInterface;
    }

    @Override
    public void onMessage(Message message) {
        try {
            cheCallbackInterface.handleCallback(message, playerKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
