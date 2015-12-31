package controller;

import com.hazelcast.core.Message;
import controller.handler.PlayerHandler;
import core.HazelcastManagerInterface;
import io.netty.channel.Channel;
import message.receive.CheMessage;
import model.client.Core;
import org.json.JSONException;
import server.CheCallbackInterface;
import util.Configuration;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by timmytime on 30/12/15.
 */
public class CheController {

    public static final String PLAYER_MAP = "PLAYER_MAP";

    //server
    private static HazelcastManagerInterface hazelcastManagerInterface;
    //utils
    private final Configuration configuration;
    //handlers
    private PlayerHandler playerHandler;
    //server up flag
    private boolean hazelcastServerUp = false;

    public CheController(Configuration configuration) throws Exception {
        this.configuration = configuration;
        hazelcastServerUp = initHazelcastServer();

    }


    private boolean initHazelcastServer() {
        try {
            hazelcastManagerInterface = (HazelcastManagerInterface) Naming.lookup(configuration.getHazelcastURL());
            playerHandler = new PlayerHandler(hazelcastManagerInterface, configuration);
            hazelcastManagerInterface.addCallback(new CheCallbackClient());
            return true;
        } catch (NotBoundException e) {
            configuration.getLogger().error("hazelcast server failed " + e.getMessage());
        } catch (MalformedURLException e) {
            configuration.getLogger().error("hazelcast server failed " + e.getMessage());
        } catch (RemoteException e) {
            configuration.getLogger().error("hazelcast server failed " + e.getMessage());
        }
        return false;
    }

    public void receive(Core message) throws RemoteException, NotBoundException, MalformedURLException, JSONException {

        //it maybe that we have rebooted it.  to be fair, if we did, then the rest is irelevant as we need to rebuild everything.
        //to think about this.  mainly im thinking to not take down hazelcast with this server, rather than other way around (unless i can rebuild server itself).
        if (hazelcastManagerInterface == null) {
            hazelcastServerUp = initHazelcastServer();
        }

        if (hazelcastServerUp) {
            configuration.getLogger().debug("handle player");
            playerHandler.handlePlayer(message);
        }

    }

    private void handleMessage(CheMessage cheMessage, String key) throws JSONException {

        configuration.getLogger().debug("we have handled");

        Channel channel = configuration.getChannelMapController().getChannel(key);

        if (!cheMessage.getRemoteAddress().equals(channel.remoteAddress().toString())) {
            configuration.getLogger().debug("we have sent");
            channel.writeAndFlush(cheMessage.getCheObject().toString());
        }
    }


    /*
      callback handler requires local access. (working around various issues with decoupling).
     */
    public class CheCallbackClient extends UnicastRemoteObject implements CheCallbackInterface {


        public CheCallbackClient() throws RemoteException {
            super();
        }

        @Override
        public void handleCallback(String message, String key) {

            configuration.getLogger().debug("we have received "+message);

            try {
                CheMessage cheMessage = new CheMessage(message);
                handleMessage(cheMessage, key);
            } catch (JSONException e) {
                configuration.getLogger().error("callback failed " + e.getMessage());
            }

        }
    }


}
