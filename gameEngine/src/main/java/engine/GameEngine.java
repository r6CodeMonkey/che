package engine;

import core.HazelcastManagerInterface;
import model.GameEngineModel;
import model.UTM;
import org.json.JSONException;
import util.Configuration;
import util.TopicPair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEngine {


    private final GameEngineUtils gameEngineUtils;
    private final MissileUtils missileUtils;
    private final Configuration configuration;

    private List<GameEngineModel> missiles = new ArrayList<>();


    public GameEngine(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        gameEngineUtils = new GameEngineUtils(hazelcastManagerInterface, configuration);
        missileUtils = new MissileUtils(gameEngineUtils, configuration);
        this.configuration = configuration;
    }


    public void removeGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException{
        gameEngineUtils.removeGameEngineModel(gameEngineModel);
    }

    public void addGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {
        gameEngineModel.getGameObject().setDistanceBetweenPoints(
                GameEnginePhysics.getHaversineDistance(
                        gameEngineModel.getGameObject().utmLocation.latitude,
                        gameEngineModel.getGameObject().utmLocation.longitude,
                        gameEngineModel.getGameObject().destinationUTMLocation.latitude,
                        gameEngineModel.getGameObject().destinationUTMLocation.longitude));

        gameEngineModel.setGameUTMLocation(gameEngineModel.getGameObject().utmLocation); //its always defaulted to this in case it doesnt move.

        //set the rules across that we need (mainly strength).
        gameEngineModel.getGameObject().strength = gameEngineModel.getGameObjectRules().getStrength();

        gameEngineUtils.addGameEngineModel(gameEngineModel);
    }




    /*
     increasing timestep may make sense (to process more) but then the accuracy is reduced.
     perhaps the user update interval is greater, whilst server faster.  plus this is all hypothetical as im running it on my PC
     likely can use AWS etc to increase resource allocation and speed, plus better cores etc.

     Update: no missiles are processed here as its real time for them...and i cba to type out comment after a reset as il update later.
     */
    public void engine() throws RemoteException {

        missiles.clear();

         Map<String,List<GameEngineModel>> movedMap = processPositions();

         for(List<GameEngineModel> gameEngineModels : movedMap.values()){
             if(gameEngineModels.size() > 0) {
                 gameEngineUtils.addToSubUTM(gameEngineModels.get(0).getGameObject().utmLocation.utm.getUtm(),
                         gameEngineModels.get(0).getGameObject().utmLocation.subUtm.getUtm(), gameEngineModels);
             }

         }
        //no.  we want to get our utm / sub utms...and keep simply remove duplicates.
        missiles.stream().forEach(missile -> missileUtils.setMissileImpactGrids(missile));
        //each missile now has a map....so need to now establish, can we group by UTMs / subUTMs.  need to reduce over processing the same grids.  to reduce rmi calls.
        Map<model.UTM, List<model.UTM>> uniqueGrids = new HashMap<>();

        //sod lambda for this well the outer part.
        for(GameEngineModel missile : missiles){
            for(UTM key : missile.getMissileTargetGrids().keySet()){
                if(uniqueGrids.containsKey(key)){
                    missile.getMissileTargetGrids().get(key).stream().filter(subUtm -> !uniqueGrids.get(key).contains(subUtm)).forEach(subUtm -> {
                        uniqueGrids.get(key).add(subUtm);
                    });
                }else{
                    uniqueGrids.put(key, missile.getMissileTargetGrids().get(key));
                }
            }

        }

        configuration.getLogger().debug("unique grids count is "+uniqueGrids.size());

        //can now process based on what we have.  note the missile maybe the only thing in the grid (and will actually be in the grid by now) but should test keys.
        for(UTM utm : uniqueGrids.keySet()){
            List<String> subUtmKeys = gameEngineUtils.getAvailableKeys(utm.getUtm());

            for(UTM subUtm : uniqueGrids.get(utm)){

                configuration.getLogger().debug("processing "+utm.getUtm()+" / "+subUtm.getUtm());

                if(subUtmKeys.contains(subUtm.getUtm())){
                    //we can process our missile (s).
                    missileUtils.processMissileImpact(
                            missiles.stream().filter(gameEngineModel ->
                                    gameEngineModel.getMissileTargetGrids().containsKey(utm) && gameEngineModel.getMissileTargetGrids().get(utm).contains(subUtm))
                                    .collect(Collectors.toList()),
                            utm.getUtm(), subUtm.getUtm());
                }
            }
        }

    }

    private Map<String,List<GameEngineModel>> processPositions() throws RemoteException {


        List<GameEngineModel> moved = new ArrayList<>();

        int total = 0;  //actually about 8000 per second. (ie 500,000 per minute).  so can reduce this further no doubt...plus not actually finished yet.

        for (String utm : configuration.getUtmConvert().getUtmGrids()) {

            //this now flies...until we fill up see AWS config...to do.
            List<String> subUtmKeys = gameEngineUtils.getAvailableKeys(utm);

            for (String subUtm : subUtmKeys) {
                List<GameEngineModel> models = gameEngineUtils.getMovingGameEngineModels(utm, subUtm);


                if (models != null && models.size() > 0) {

                    total += models.size();

                    models.parallelStream().forEach(model -> GameEnginePhysics.process(model, configuration.getUtmConvert(), configuration.getGameEngineDelta()));

                    //ok we reall need to collect all the objects that are fixed now....not this will be slow
                    new Thread(() -> {
                        models.parallelStream().filter(gameEngineModel -> gameEngineModel.getGameObject().getDistanceBetweenPoints() == 0).forEach((gameEngineModel1) -> {
                            try {
                                //need a bulk version
                                gameEngineUtils.updatePlayer(gameEngineModel1);
                                //capture our missiles that have reached target.
                                if(gameEngineModel1.isMissile()){
                                    missiles.add(gameEngineModel1);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        });
                    }).start();


                    ConcurrentMap<Boolean, List<GameEngineModel>> updated =
                            models.parallelStream().collect(Collectors.groupingByConcurrent(GameEngineModel::hasChangedGrid));

                    if (updated.get(Boolean.FALSE) != null) {

                        updated.get(Boolean.FALSE).stream().forEach(model -> {
                            try {
                                gameEngineUtils.processMoveMessage(model);
                            } catch (JSONException e) {
                                configuration.getLogger().error("json exception " + e.getMessage());
                            }
                        });

                        gameEngineUtils.updateSubUTM(utm, subUtm, updated.get(Boolean.FALSE));
                        gameEngineUtils.bulkPublish(utm + subUtm, updated.get(Boolean.FALSE));

                        configuration.getLogger().debug("object has not moved grids");
                    }

                    if (updated.get(Boolean.TRUE) != null) {

                        configuration.getLogger().debug("object has moved grids");

                        updated.get(Boolean.TRUE).stream().forEach(model -> {
                            try {
                                gameEngineUtils.processMoveMessage(model);
                            } catch (JSONException e) {
                                configuration.getLogger().error("json exception " + e.getMessage());
                            }
                        });

                        gameEngineUtils.bulkUnSubscribe(utm, subUtm, updated.get(Boolean.TRUE));
                        gameEngineUtils.bulkSubscribe(updated.get(Boolean.TRUE));

                        new Thread(() -> {
                            try {
                                gameEngineUtils.bulkRemoveGameEngineModel(utm, subUtm, updated.get(Boolean.TRUE));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }).start();


                        moved.addAll(updated.get(Boolean.TRUE).stream().collect(Collectors.toList()));

                    }

                }
            }

        }

        configuration.getLogger().debug("total records processed " + total);

        return moved.parallelStream().collect(Collectors.groupingBy(GameEngineModel::getUTMKey));

    }


}
