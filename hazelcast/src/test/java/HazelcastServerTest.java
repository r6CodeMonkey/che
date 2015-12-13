import core.HazelcastManager;
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
import java.rmi.registry.LocateRegistry;

import static org.junit.Assert.assertEquals;

/**
 * Created by timmytime on 13/12/15.
 */
public class HazelcastServerTest {

    private static  HazelcastManagerInterface hazelcastManagerInterface;

    @BeforeClass
    public static void init() throws RemoteException, NotBoundException,MalformedURLException{
        Configuration configuration = new Configuration();

        try{
            LocateRegistry.createRegistry(configuration.getPort());
        }catch(Exception e){

        }

        HazelcastServer server = new HazelcastServer();
        Naming.rebind(configuration.getURL(), server);

        hazelcastManagerInterface = (HazelcastManagerInterface) Naming.lookup(configuration.getURL());
    }

    @Test
    public void testServer() throws RemoteException, NotBoundException,MalformedURLException {
        hazelcastManagerInterface.createMap("test");
        hazelcastManagerInterface.put("test", "key", new String("tim"));
        assertEquals("tim", hazelcastManagerInterface.get("test", "key").toString());
    }

    @AfterClass
    public static void shutDown(){
        HazelcastManager.stop();
    }
}
