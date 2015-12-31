package controller.handler;

import core.HazelcastManagerInterface;

import model.client.generic.GenericModel;
import util.Configuration;

/**
 * Created by timmytime on 31/12/15.
 */
public class GenericHandler {

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;

    public GenericHandler(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration){
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;

    }

    public void handle(GenericModel genericModel){

        switch (genericModel.getType()){
            /*need to handles and load various types...or simply do whatever,..
            really we want to pass this stuff?  needs thought.

            basically building in logic rather than loading but can reboot it...assuming schema updated....so use schema maps.

            */
            default:
                return;
        }

    }

}
