package util;

import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import server.CheCallbackInterface;

import java.rmi.RemoteException;

/**
 * Created by timmytime on 31/12/15.
 */
public class CheMessageHandler implements MessageListener {

    private final String playerKey;
    private final CheCallbackInterface cheCallbackInterface;

    public CheMessageHandler(CheCallbackInterface cheCallbackInterface, String playerKey) {
        this.playerKey = playerKey;
        this.cheCallbackInterface = cheCallbackInterface;
    }

    @Override
    public void onMessage(Message message) {

        //http://docs.hazelcast.org/docs/3.5/manual/html/globaleventconfiguration.html  note we hit this at 10000 connections, all calling same topic.  at same time
        //so its pretty dumb test.  but need to review configuration of it.  is weak point at present.

        //need to think about thread management too.
        try {
            //hazelcast doesnt like rmi.
            String msg = message.getMessageObject().toString();

            new Thread(() -> {
                try {
                    //threading has reduced issue...but review config.
                    cheCallbackInterface.handleCallback(msg, playerKey);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();  //should really log on configuration. logger could be static.  anyway.
        }
    }
}
