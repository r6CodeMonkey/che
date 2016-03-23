package engine;

import core.HazelcastManagerInterface;
import model.GameEngineModel;
import org.json.JSONException;
import util.Configuration;
import util.Tags;
import util.TopicPair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEngine {


    private final GameEngineUtils gameEngineUtils;
    private final Configuration configuration;


    public GameEngine(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        gameEngineUtils = new GameEngineUtils(hazelcastManagerInterface, configuration);
        this.configuration = configuration;
    }


    public void addGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {
        gameEngineModel.getGameObject().setDistanceBetweenPoints(
                GameEnginePhysics.getHaversineDistance(
                        gameEngineModel.getGameObject().utmLocation.latitude,
                        gameEngineModel.getGameObject().utmLocation.longitude,
                        gameEngineModel.getGameObject().destinationUTMLocation.latitude,
                        gameEngineModel.getGameObject().destinationUTMLocation.longitude));

        gameEngineUtils.addGameEngineModel(gameEngineModel);
    }


    /*
     increasing timestep may make sense (to process more) but then the accuracy is reduced.
     perhaps the user update interval is greater, whilst server faster.  plus this is all hypothetical as im running it on my PC
     likely can use AWS etc to increase resource allocation and speed, plus better cores etc.

     Update: no missiles are processed here as its real time for them...and i cba to type out comment after a reset as il update later.
     */
    public void engine() throws RemoteException {

        ConcurrentMap<TopicPair, List<GameEngineModel>> movedMap = processPositions();

        for (TopicPair topicPair : movedMap.keySet()) {
            gameEngineUtils.addToSubUTM(topicPair.getKey(), topicPair.getTopicKey(), movedMap.get(topicPair));
        }

    }

    private ConcurrentMap<TopicPair, List<GameEngineModel>> processPositions() throws RemoteException {


        List<GameEngineModel> moved = new ArrayList<>();

        int total = 0;  //actually about 8000 per second. (ie 500,000 per minute).  so can reduce this further no doubt...plus not actually finished yet.

        for (String utm : configuration.getUtmConvert().getUtmGrids()) {

            //this now flies...until we fill up see AWS config...to do.
            List<String> subUtmKeys = gameEngineUtils.getAvailableKeys(utm);

            for (String subUtm : subUtmKeys) {
                List<GameEngineModel> models = gameEngineUtils.getGameEngineModels(utm, subUtm);


                if (models != null && models.size() > 0) {

                    total += models.size();

                    models.parallelStream().forEach(model -> GameEnginePhysics.process(model, configuration.getUtmConvert(), configuration.getGameEngineDelta()));

                    //ok we reall need to collect all the objects that are fixed now....not this will be slow
                    new Thread(() -> {
                     models.parallelStream().filter(gameEngineModel -> gameEngineModel.getGameObject().getDistanceBetweenPoints() == 0).forEach((gameEngineModel1) -> {
                        try {
                            //need a bulk version
                            gameEngineUtils.updatePlayer(gameEngineModel1);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });}).start();

                    models.stream().forEach(gameEngineModel -> configuration.getLogger().debug("game location "+gameEngineModel.getGameUTMLocation().latitude+"/"+gameEngineModel.getGameUTMLocation().longitude+
                    " \nversus game object "+gameEngineModel.getGameObject().utmLocation.latitude+"/"+gameEngineModel.getGameObject().utmLocation.longitude));

                    ConcurrentMap<Boolean, List<GameEngineModel>> updated =
                            models.parallelStream().collect(Collectors.groupingByConcurrent(GameEngineModel::hasChangedGrid));

                    if (updated.get(Boolean.FALSE) != null) {

                            updated.get(Boolean.FALSE).stream().forEach(model -> {
                                try {
                                    gameEngineUtils.processMoveMessage(model);
                                } catch (JSONException e) {
                                   configuration.getLogger().error("json exception "+e.getMessage());
                                }
                            });

                        gameEngineUtils.updateSubUTM(utm, subUtm, updated.get(Boolean.FALSE));
                        gameEngineUtils.bulkPublish(utm + subUtm, updated.get(Boolean.FALSE));

                        configuration.getLogger().debug("object has not moved grids");
                    }

                    if (updated.get(Boolean.TRUE) != null) {

                        configuration.getLogger().debug("object has moved grids");

                        gameEngineUtils.bulkUnSubscribe(utm, subUtm, updated.get(Boolean.TRUE));
                        gameEngineUtils.bulkSubscribe(updated.get(Boolean.TRUE));

                        new Thread(() -> {
                            try {
                                gameEngineUtils.bulkRemoveGameEngineModel(utm, subUtm, updated.get(Boolean.TRUE));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }).start();

                            updated.get(Boolean.TRUE).stream().forEach(model -> {
                                try {
                                    gameEngineUtils.processMoveMessage(model);
                                } catch (JSONException e) {
                                    configuration.getLogger().error("json exception "+e.getMessage());
                                }
                            });

                        moved.addAll(updated.get(Boolean.TRUE).stream().collect(Collectors.toList()));

                    }

                }
            }

        }

        configuration.getLogger().debug("total records processed " + total);

        //probably doesnt need concurrent as not filtering...its simply a collect all.  anyway.
        return moved.parallelStream().collect(Collectors.groupingByConcurrent(gameEngineModel ->
                new TopicPair(gameEngineModel.getGameObject().utmLocation.utm.getUtm(), gameEngineModel.getGameObject().utmLocation.subUtm.getUtm(), null)));


    }


}
