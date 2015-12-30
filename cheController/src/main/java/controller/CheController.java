package controller;

import core.HazelcastManagerInterface;
import model.Core;
import util.Configuration;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by timmytime on 30/12/15.
 */
public class CheController {

    private static HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;
    private boolean hazelcastServerUp = false;

    public CheController(Configuration configuration) throws Exception {
        this.configuration = configuration;
        hazelcastServerUp = initHazelcastServer();
    }

    private boolean initHazelcastServer() {
        try {
            hazelcastManagerInterface = (HazelcastManagerInterface) Naming.lookup(configuration.getHazelcastURL());
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

    public void receive(Core message) throws RemoteException, NotBoundException, MalformedURLException {

        //it maybe that we have rebooted it.  to be fair, if we did, then the rest is irelevant as we need to rebuild everything.
        //to think about this.  mainly im thinking to not take down hazelcast with this server, rather than other way around (unless i can rebuild server itself).
        if (hazelcastManagerInterface == null) {
            hazelcastServerUp = initHazelcastServer();
        }

        if (hazelcastServerUp) {
            //switch the cases...and carry out the actions.
        }


    }

}
