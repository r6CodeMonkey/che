package com.oddymobstar.server.netty.store;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.oddymobstar.server.netty.channel.handler.ChannelHandler;

/**
 * Created by timmytime on 18/02/15.
 */
public class AllianceStore implements ManagerInterface {

    private HazelcastInstance allianceChannels;

    public AllianceStore() {
        allianceChannels = Hazelcast.newHazelcastInstance();
    }


    @Override
    public ITopic getTopic(String id) {
        return allianceChannels.getTopic(id);
    }

    @Override
    public void removeFromTopic(String id, String regID) {
        allianceChannels.getTopic(id).removeMessageListener(regID);
    }

    @Override
    public String subscribe(String id, ChannelHandler player) {
        return allianceChannels.getTopic(id).addMessageListener(player);

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
        allianceChannels.shutdown();
    }
}
