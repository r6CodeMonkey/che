package game;

import com.hazelcast.core.IMap;
import core.HazelcastManagerInterface;
import util.Configuration;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEngine {

    public static final String GAME_ENGINE_MOVE_MAP = "GAME_ENGINE_MOVE";

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;


    public GameEngine(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
    }

    public void addGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {
        List<GameEngineModel> subUtmList = (List<GameEngineModel>) this.hazelcastManagerInterface.get(gameEngineModel.getGameObject().utmLocation.utm.getUtm()).get(gameEngineModel.getGameObject().utmLocation.subUtm.getUtm());

        if (subUtmList == null) {
            subUtmList = new ArrayList<>();
            subUtmList.add(gameEngineModel);
        } else {
            subUtmList.add(gameEngineModel);
        }

        hazelcastManagerInterface.get(gameEngineModel.getGameObject().utmLocation.utm.getUtm()).put(gameEngineModel.getGameObject().utmLocation.subUtm.getUtm(), subUtmList);
    }

    public void removeGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {
        //is an assumption it exists..so maybe dont check?
        try {
            ((List<GameEngineModel>) hazelcastManagerInterface.get(gameEngineModel.getGameObject().utmLocation.utm.getUtm()).get(gameEngineModel.getGameObject().utmLocation.subUtm.getUtm()))
                    .remove(gameEngineModel);
        } catch (Exception e) {
            configuration.getLogger().debug("remove game engine failed as perhaps it was not in the list " + e.getMessage());
        }

    }

    /*
      next we need our loop....basically we move an object, ue update its current lat, long and then utm and sub utm based on rules.
     */
    private void engine() throws RemoteException {

        for (String utm : configuration.getUtmConvert().getUtmGrids()) {

            IMap utmMap = hazelcastManagerInterface.get(utm);
            //now actually we need the sub utms..which are a map of lists...oops
            Map<String, List<GameEngineModel>> subUtms = (Map<String, List<GameEngineModel>>) utmMap.values();

            for(List<GameEngineModel> subUtm : subUtms.values()) {

                for (GameEngineModel gameEngineModel : subUtm) {

                    GameEnginePhysics.process(gameEngineModel, configuration.getUtmConvert(), configuration.getGameEngineDelta());

                    if (!gameEngineModel.getGameUTMLocation().utm.getUtm().equals(gameEngineModel.getGameObject().utmLocation.utm.getUtm())
                            || !gameEngineModel.getGameUTMLocation().subUtm.getUtm().equals(gameEngineModel.getGameObject().utmLocation.subUtm.getUtm())) {
                        //we have changed....as we only register in sub utms...simply remove from current, add to new...does mean user gets shit from it but heyho.
                        hazelcastManagerInterface.unSubscribe(utm + gameEngineModel.getGameUTMLocation().subUtm.getUtm(), gameEngineModel.getGameObject().getTopicSubscriptions());
                        gameEngineModel.getGameObject().getTopicSubscriptions().addSubscription(gameEngineModel.getGameUTMLocation().utm.getUtm() + gameEngineModel.getGameUTMLocation().subUtm.getUtm(),
                                hazelcastManagerInterface.subscribe(gameEngineModel.getGameUTMLocation().utm.getUtm() + gameEngineModel.getGameUTMLocation().subUtm.getUtm(), gameEngineModel.getPlayerKey()));
                        gameEngineModel.getGameObject().utmLocation = gameEngineModel.getGameUTMLocation();
                        removeGameEngineModel(gameEngineModel);
                        //we now need to add to our temp map...in order to sort out once all processed.  a list would of been better key pointless.
                        hazelcastManagerInterface.get(GAME_ENGINE_MOVE_MAP).put(gameEngineModel.getGameObject().getKey(), gameEngineModel);
                    } else {
                        //can simply update our utm location..we would lose altitude etc but not using that anyway..or speed.
                        gameEngineModel.getGameObject().utmLocation = gameEngineModel.getGameUTMLocation();
                    }

                }
            }

        }

        //add our moved objects back in so they dont get double moved.
        IMap utmMap = hazelcastManagerInterface.get(GAME_ENGINE_MOVE_MAP);

        Set<String> keys = utmMap.keySet();

        for (String key : keys) {
            addGameEngineModel((GameEngineModel) utmMap.get(key));  //ensure this is safe lolz.
        }

        //all good.  now basically find out collision impacts....again we do this by utm / sub utm....
        for (String utm : configuration.getUtmConvert().getUtmGrids()) {

            utmMap = hazelcastManagerInterface.get(utm);

            //actually wrong.

            //see above.  is fixed now.  we can also write some good tests for this (well bar the hazel cast part but can ignore if it runs).
        }


    }


}
