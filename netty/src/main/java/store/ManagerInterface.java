package store;

import com.hazelcast.core.ITopic;
import channel.handler.ChannelHandler;

/**
 * Created by timmytime on 18/02/15.
 */
public interface ManagerInterface {

    public ITopic getTopic(String id);

    public void removeFromTopic(String id, String regID);

    public String subscribe(String id, ChannelHandler channel);

    public void add(Object object);

    public void remove(String id);

    public Object get(String id);

    public void stop();
}
