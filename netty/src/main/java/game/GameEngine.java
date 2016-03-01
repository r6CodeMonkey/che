package game;

import com.hazelcast.core.IMap;
import core.HazelcastManagerInterface;
import model.GameEngineModel;
import model.UTM;
import util.Configuration;
import util.MapPair;
import util.TopicPair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEngine {


    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;


    public GameEngine(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
    }

    /*
     need to be able to put full lists back in as well. its more efficient over rmi.
     */
    public void updateSubUTM(String utm, String subUtm,  List<GameEngineModel> gameEngineModels) throws RemoteException{
        hazelcastManagerInterface.put(utm, subUtm, gameEngineModels);
    }

    public void addToSubUTM(String utm, String subUtm,List<GameEngineModel> gameEngineModels ) throws RemoteException{
        List<GameEngineModel> subUtmList = (List<GameEngineModel>) hazelcastManagerInterface
                .get(utm, subUtm);

        if(subUtmList != null && subUtmList.size() > 0) {
            subUtmList.addAll(gameEngineModels.stream().collect(Collectors.toList()));
            hazelcastManagerInterface.put(utm, subUtm, subUtmList);
        }else{
            updateSubUTM(utm, subUtm, gameEngineModels);
        }
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

    public void bulkRemoveGameEngineModel(String utmKey, String subUtmKey, List<GameEngineModel> gameEngineModels) throws RemoteException{
        try {
            List<GameEngineModel> subUtmList = (List<GameEngineModel>) hazelcastManagerInterface.get(utmKey, subUtmKey);
            gameEngineModels.forEach(subUtmList::remove);
            hazelcastManagerInterface.put(utmKey, subUtmKey, subUtmList);

        } catch (Exception e) {
            configuration.getLogger().debug("remove game engine failed as perhaps it was not in the list " + e.getMessage());
        }

    }

    /*
    main access
     */
    public void engine() throws RemoteException {


        ConcurrentMap<TopicPair, List<GameEngineModel>> movedMap = processPositions();

        for(TopicPair topicPair : movedMap.keySet()){
            addToSubUTM(topicPair.getKey(), topicPair.getTopicKey(), movedMap.get(topicPair));
        }

        processMissiles();

    }

    public ConcurrentMap<TopicPair, List<GameEngineModel>> processPositions() throws RemoteException {


        List<GameEngineModel> moved = new ArrayList<>();

        int total = 0;  //actually about 8000 per second. (ie 500,000 per minute).  so can reduce this further no doubt...plus not actually finished yet.

        for (String utm : configuration.getUtmConvert().getUtmGrids()) {

            //this now flies...until we fill up see AWS config...to do.
            List<String> subUtmKeys = ( List<String>)hazelcastManagerInterface.getAvailableKeys(utm);

            for (String subUtm : subUtmKeys) {
                List<GameEngineModel> models = (List<GameEngineModel>) hazelcastManagerInterface.get(utm, subUtm);


                if (models != null  && models.size() > 0) {

                    total += models.size();

                    models.parallelStream().forEach(model -> GameEnginePhysics.process(model, configuration.getUtmConvert(), configuration.getGameEngineDelta()));

                    ConcurrentMap<Boolean, List<GameEngineModel>> updated =
                            models.parallelStream().collect(Collectors.groupingByConcurrent(GameEngineModel::hasChangedGrid));

                    if(updated.get(Boolean.FALSE) != null) {
                        updateSubUTM(utm, subUtm, updated.get(Boolean.FALSE));
                    }

                    if(updated.get(Boolean.TRUE) != null){

                        new Thread(() -> {
                            try {
                                hazelcastManagerInterface.bulkUnSubscribe(updated.get(Boolean.TRUE).stream().map(model -> new TopicPair(model.getPlayerKey(),
                                        utm + subUtm,
                                        model.getGameObject().getTopicSubscriptions())).collect(Collectors.toList()));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }).start();


                        new Thread(() -> {
                            try {
                                hazelcastManagerInterface.bulkSubscribe(updated.get(Boolean.TRUE).stream().map(model -> new TopicPair(model.getPlayerKey(),
                                        model.getGameUTMLocation().utm.getUtm() + model.getGameUTMLocation().subUtm.getUtm(),
                                        model.getGameObject().getTopicSubscriptions())).collect(Collectors.toList()));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }).start();

                        new Thread(() -> {
                            try {
                                bulkRemoveGameEngineModel(utm, subUtm, updated.get(Boolean.TRUE));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }).start();

                        new Thread(() -> {
                            updated.get(Boolean.TRUE).stream().forEach(model -> model.getGameObject().utmLocation = model.getGameUTMLocation());
                        }).start();

                        moved.addAll(updated.get(Boolean.TRUE).stream().collect(Collectors.toList()));

                    }

                }
            }

        }

        configuration.getLogger().debug("total records processed "+total);

        return moved.parallelStream().collect(Collectors.groupingByConcurrent(gameEngineModel ->
                new TopicPair(gameEngineModel.getGameObject().utmLocation.utm.getUtm(),gameEngineModel.getGameObject().utmLocation.subUtm.getUtm(),null)));


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
