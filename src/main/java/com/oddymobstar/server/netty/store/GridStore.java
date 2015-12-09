package com.oddymobstar.server.netty.store;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import com.oddymobstar.server.netty.channel.handler.ChannelHandler;
import com.oddymobstar.server.netty.util.UTM;
import com.oddymobstar.server.netty.util.UTMConvert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by timmytime on 18/02/15.
 */
public class GridStore implements ManagerInterface {


    private HazelcastInstance grids;
    private HazelcastInstance gridChannels;




    public GridStore() {

        grids = Hazelcast.newHazelcastInstance();
        gridChannels = Hazelcast.newHazelcastInstance();

        //60by20 grids.  each grid is then sub gridded within.  Therefore store a new Map inside it.
        //ie each grid is Map<String, Map<GridObject>
        for (int i = 0; i < UTMConvert.LONG_ZONES; i++) {
            for (String j : UTMConvert.latValues) {
                grids.getMap(j + i);
            }
        }

    }

    @Override
    public ITopic getTopic(String id) {
        return gridChannels.getTopic(id);
    }

    @Override
    public void removeFromTopic(String id, String regID) {
        //is utm info.
        gridChannels.getTopic(id).removeMessageListener(regID);
    }

    @Override
    public String subscribe(String id, ChannelHandler channel) {
        return gridChannels.getTopic(id).addMessageListener(channel);
    }

    @Override
    public void add(Object object) {
        //cant use
    }

    public void addSubUtm(UTM utm, UTM subUtm) {
        if (grids.getMap(utm.getUtm()).get(subUtm.getUtm()) == null) {
            grids.getMap(utm.getUtm()).put(subUtm.getUtm(), new HashMap<String, Object>());
        }
    }

    public void addItem(UTM utm, UTM subUtm, String key, Object object) {

        Map<String, Object> subUtmMap = (HashMap<String, Object>) grids.getMap(utm.getUtm()).get(subUtm.getUtm());
        if (subUtmMap != null) {
            if (subUtmMap.get(key) == null) {
                subUtmMap.put(key, object);
            }
        } else {
            subUtmMap = new HashMap<>();
            grids.getMap(utm.getUtm()).put(subUtm.getUtm(), subUtmMap);
        }
    }

    @Override
    public void remove(String id) {
        //cant use
    }

    public void removeGrid(String utm) {
        grids.getMap(utm).evictAll();
    }

    public void removeSubGrid(UTM utm, UTM subUtm) {
        grids.getMap(utm.getUtm()).remove(subUtm.getUtm());
    }

    public void removeSubGridItem(UTM utm, UTM subUtm, String key) {

         /*     JobTracker jobTracker = grids.getJobTracker( "default" );

        IMap<String, Object> map = grids.getMap(utm.getUtm());
        KeyValueSource<String, Object> source = KeyValueSource.fromMap(map);
        Job<String, Object> job = jobTracker.newJob(source);
     */


        Map<String, Object> subUtmMap = (Map) grids.getMap(utm.getUtm()).get(subUtm.getUtm());
        if (subUtmMap != null) {
            subUtmMap.remove(key);
            if (subUtmMap.size() == 0) {
                removeSubGrid(utm, subUtm);
            }
        }
    }


    public IMap getGridMap(UTM utm) {
        return grids.getMap(utm.getUtm());
    }

    public Map<String, Object> getSubGridMap(UTM utm, UTM subUtm) {
        return (Map<String, Object>) grids.getMap(utm.getUtm()).get(subUtm.getUtm());
    }

    @Override
    public Object get(String id) {
        return null;  //cant use either
    }

    @Override
    public void stop() {
        grids.shutdown();
        gridChannels.shutdown();
    }
}
