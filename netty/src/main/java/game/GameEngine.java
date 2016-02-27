package game;

import com.hazelcast.core.IMap;
import core.HazelcastManagerInterface;
import model.GameEngineModel;
import util.Configuration;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
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

    public void updateGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException{
        List<GameEngineModel> subUtmList = (List<GameEngineModel>) hazelcastManagerInterface
                .get(gameEngineModel.getGameObject().utmLocation.utm.getUtm(), gameEngineModel.getGameObject().utmLocation.subUtm.getUtm());

         subUtmList.remove(gameEngineModel);
        subUtmList.add(gameEngineModel);

        hazelcastManagerInterface.put(gameEngineModel.getGameObject().utmLocation.utm.getUtm(), gameEngineModel.getGameObject().utmLocation.subUtm.getUtm(), subUtmList);

    }


    public void addGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {
        List<GameEngineModel> subUtmList = (List<GameEngineModel>) hazelcastManagerInterface
                .get(gameEngineModel.getGameObject().utmLocation.utm.getUtm(), gameEngineModel.getGameObject().utmLocation.subUtm.getUtm());

        if (subUtmList == null) {
            subUtmList = new ArrayList<>();
            subUtmList.add(gameEngineModel);
        } else {
            subUtmList.add(gameEngineModel);
        }

        hazelcastManagerInterface.put(gameEngineModel.getGameObject().utmLocation.utm.getUtm(), gameEngineModel.getGameObject().utmLocation.subUtm.getUtm(), subUtmList);
    }

    public void removeGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {
        //is an assumption it exists..so maybe dont check?
        try {
            List<GameEngineModel> subUtmList = (List<GameEngineModel>) hazelcastManagerInterface.get(gameEngineModel.getGameObject().utmLocation.utm.getUtm(), gameEngineModel.getGameObject().utmLocation.subUtm.getUtm());
            subUtmList.remove(gameEngineModel);
            hazelcastManagerInterface.put(gameEngineModel.getGameObject().utmLocation.utm.getUtm(), gameEngineModel.getGameObject().utmLocation.subUtm.getUtm(), subUtmList);

        } catch (Exception e) {
            configuration.getLogger().debug("remove game engine failed as perhaps it was not in the list " + e.getMessage());
        }

    }

    /*
      next we need our loop....basically we move an object, ue update its current lat, long and then utm and sub utm based on rules.
     */
    public void engine() throws RemoteException {


        processPositions();

        updateMaps();

        processMissiles();

    }

    public void processPositions() throws RemoteException {
        for (String utm : configuration.getUtmConvert().getUtmGrids()) {

            //this now flies...until we fill up see AWS config...to do.
            List<String> subUtmKeys = ( List<String>)hazelcastManagerInterface.getAvailableKeys(utm);

            for (String subUtm : subUtmKeys) {
                configuration.getLogger().debug("subutm is " + subUtm);
                List<GameEngineModel> models = (List<GameEngineModel>) hazelcastManagerInterface.get(utm, subUtm);

                if (models != null) {
                    for (GameEngineModel gameEngineModel : models) {
                        GameEnginePhysics.process(gameEngineModel, configuration.getUtmConvert(), configuration.getGameEngineDelta());

                        System.out.println("distance is " + gameEngineModel.getGameObject().getDistanceBetweenPoints());


                        if (!gameEngineModel.getGameUTMLocation().utm.getUtm().equals(gameEngineModel.getGameObject().utmLocation.utm.getUtm())
                                || !gameEngineModel.getGameUTMLocation().subUtm.getUtm().equals(gameEngineModel.getGameObject().utmLocation.subUtm.getUtm())) {

                            //we have changed....as we only register in sub utms...simply remove from current, add to new...does mean user gets shit from it but heyho.
                            hazelcastManagerInterface.unSubscribe(utm + gameEngineModel.getGameUTMLocation().subUtm.getUtm(), gameEngineModel.getGameObject().getTopicSubscriptions());
                            gameEngineModel.getGameObject().getTopicSubscriptions().addSubscription(gameEngineModel.getGameUTMLocation().utm.getUtm() + gameEngineModel.getGameUTMLocation().subUtm.getUtm(),
                                    hazelcastManagerInterface.subscribe(gameEngineModel.getGameUTMLocation().utm.getUtm() + gameEngineModel.getGameUTMLocation().subUtm.getUtm(), gameEngineModel.getPlayerKey()));
                            removeGameEngineModel(gameEngineModel);
                            gameEngineModel.getGameObject().utmLocation = gameEngineModel.getGameUTMLocation();
                            //to test..
                            hazelcastManagerInterface.put(GAME_ENGINE_MOVE_MAP, gameEngineModel.getGameObject().getKey(), gameEngineModel);
                            configuration.getLogger().debug("we have changed grids");
                        } else {
                            configuration.getLogger().debug("we have not changed grids");
                            //can simply update our utm location..we would lose altitude etc but not using that anyway..or speed.
                            gameEngineModel.getGameObject().utmLocation = gameEngineModel.getGameUTMLocation();
                            //now we really need to add it back with latest information
                            updateGameEngineModel(gameEngineModel);
                        }

                    }
                }
            }

        }

    }

    public void updateMaps() throws RemoteException {
        //add our moved objects back in so they dont get double moved.
        IMap utmMap = null;//hazelcastManagerInterface.get(GAME_ENGINE_MOVE_MAP);

        Set<String> keys = utmMap.keySet();

        for (String key : keys) {
            addGameEngineModel((GameEngineModel) utmMap.get(key));  //ensure this is safe lolz.
        }
    }

    public void processMissiles() throws RemoteException {
        //all good.  now basically find out collision impacts....again we do this by utm / sub utm....
        for (String utm : configuration.getUtmConvert().getUtmGrids()) {

            IMap utmMap = null;//hazelcastManagerInterface.get(utm);

            //actually wrong.

            //see above.  is fixed now.  we can also write some good tests for this (well bar the hazel cast part but can ignore if it runs).
        }

    }


}
