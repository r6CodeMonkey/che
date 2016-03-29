package engine;

import model.GameEngineModel;
import model.GameObject;
import org.json.JSONException;
import util.Configuration;
import util.map.GridCreator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by timmytime on 28/03/16.
 */
public class MissileUtils {


    private final Configuration configuration;
    private final GameEngineUtils gameEngineUtils;
    private final GridCreator gridCreator;

    public MissileUtils(GameEngineUtils gameEngineUtils, Configuration configuration) {
        this.gameEngineUtils = gameEngineUtils;
        this.configuration = configuration;
        this.gridCreator = new GridCreator(configuration.getUtmConvert());
    }

    public void setMissileImpactGrids(GameEngineModel gameEngineModel) {
        gameEngineModel.setMissileTargetGrids(gridCreator.getGrids(3, gameEngineModel.getGameUTMLocation().latitude, gameEngineModel.getGameUTMLocation().longitude));
    }

    public void processMissileImpact(List<GameEngineModel> missiles, String utm, String subUtm) throws RemoteException {

        //so go and get all the objects in that sector
        List<GameEngineModel> models = gameEngineUtils.getGameEngineModels(utm, subUtm);

        for (GameEngineModel missile : missiles) {

            List<GameEngineModel> hitTargets = models.stream().filter(gameEngineModel ->
                    GameEnginePhysics.getHaversineDistance(
                            gameEngineModel.getGameUTMLocation().latitude, gameEngineModel.getGameUTMLocation().longitude,
                            missile.getGameUTMLocation().latitude, missile.getGameUTMLocation().longitude) < missile.getGameObjectRules().getImpactRadius()
                            && !gameEngineModel.isMissile()).collect(Collectors.toList());


            hitTargets.stream().forEach(gameEngineModel1 ->
              gameEngineModel1.getGameObject().strength -= (missile.getGameObjectRules().getStrength() *
                      ((missile.getGameObjectRules().getImpactRadius() -
                      GameEnginePhysics.getHaversineDistance(
                              gameEngineModel1.getGameUTMLocation().latitude, gameEngineModel1.getGameUTMLocation().longitude,
                              missile.getGameUTMLocation().latitude, missile.getGameUTMLocation().longitude))
                              / missile.getGameObjectRules().getImpactRadius())));

             hitTargets.stream().forEach(gameEngineModel -> configuration.getLogger().debug("we have a hit target " + gameEngineModel.getGameObject().getKey()+" and the strength is now "+gameEngineModel.getGameObject().strength));


            hitTargets.stream().forEach(gameEngineModel -> {
                try {
                    gameEngineUtils.processHitMessage(gameEngineModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            gameEngineUtils.bulkPublish(utm+subUtm, hitTargets); //we dont have a points system yet.  and do we even need one?  yes most likely.
            //but add this at end.
        }

    }



}
