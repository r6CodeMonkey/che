import core.HazelcastManagerInterface;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import server.HazelcastServer;
import util.Configuration;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 13/12/15.
 */
public class HazelcastServerTest {

    private static HazelcastManagerInterface hazelcastManagerInterface;

    @BeforeClass
    public static void init() throws Exception {
        Configuration configuration = new Configuration();

        HazelcastServer.startServer();
        hazelcastManagerInterface = (HazelcastManagerInterface) Naming.lookup(configuration.getURL());
    }

    @AfterClass
    public static void shutDown() {
        HazelcastServer.stopServer();
    }

    @Test
    public void testServer() throws RemoteException, NotBoundException, MalformedURLException {
        hazelcastManagerInterface.createMap("test");
        hazelcastManagerInterface.put("test", "key", new String("tim"));
        assertEquals("tim", hazelcastManagerInterface.get("test", "key").toString());

     /*   NettyChannelHandler nettyChannelHandler = new NettyChannelHandler(mock(Channel.class));
        TopicSubscriptions topicSubscriptions = new TopicSubscriptions();

        topicSubscriptions.addSubscription("test", hazelcastManagerInterface.subscribe("test", nettyChannelHandler));

   same issue.  basically, everything goes together, or needs to be decoupled (which was my original aim).

   need to think...as long as netty has the channel, we simply need to communicate between that and everything else.

   //so option how do you communicate back to netty?  interesting conundrum.

   ie:  mobile -> netty -> ?? -> controller -> hazelcast && hadoop

   so create 2 sockets (not good) or create 1 socket, and then contact it back on the netty server.


*/

    }
}
