package store;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import channel.handler.ChannelHandler;

/**
 * Created by timmytime on 18/02/15.
 */
public class PackageStore implements ManagerInterface {

    private HazelcastInstance packageChannels;

    public PackageStore() {
        packageChannels = Hazelcast.newHazelcastInstance();

    }

    @Override
    public ITopic getTopic(String id) {
        return packageChannels.getTopic(id);
    }

    @Override
    public void removeFromTopic(String id, String regID) {
        packageChannels.getTopic(id).removeMessageListener(regID);
    }

    @Override
    public String subscribe(String id, ChannelHandler channel) {
        return packageChannels.getTopic(id).addMessageListener(channel);
    }

    @Override
    public void add(Object object) {

    }

    @Override
    public void remove(String id) {

    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public void stop() {
        packageChannels.shutdown();
    }
}
