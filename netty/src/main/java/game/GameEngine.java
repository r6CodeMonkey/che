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
        List<GameEngineModel> subUtmList = (List<GameEngineModel>)this.hazelcastManagerInterface.get(gameEngineModel.getGameObject().utmLocation.utm.getUtm()).get(gameEngineModel.getGameObject().utmLocation.subUtm.getUtm());

        if(subUtmList == null){
            subUtmList = new ArrayList<>();
            subUtmList.add(gameEngineModel);
        }else{
            subUtmList.add(gameEngineModel);
        }

        hazelcastManagerInterface.get(gameEngineModel.getGameObject().utmLocation.utm.getUtm()).put(gameEngineModel.getGameObject().utmLocation.subUtm.getUtm(), subUtmList);
    }

    public void removeGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {
        //is an assumption it exists..so maybe dont check?
        try {
            ((List<GameEngineModel>) hazelcastManagerInterface.get(gameEngineModel.getGameObject().utmLocation.utm.getUtm()).get(gameEngineModel.getGameObject().utmLocation.subUtm.getUtm()))
                    .remove(gameEngineModel);
        }catch (Exception e){
            configuration.getLogger().debug("remove game engine failed as perhaps it was not in the list "+e.getMessage());
        }

    }

    /*
      next we need our loop....basically we move an object, ue update its current lat, long and then utm and sub utm based on rules.
     */
    private void engine() throws RemoteException {

        for(String utm : configuration.getUtmConvert().getUtmGrids()){

           IMap utmMap = hazelcastManagerInterface.get(utm);

           for(GameEngineModel gameEngineModel : (List<GameEngineModel>)utmMap.values()){

               GameEnginePhysics.process(gameEngineModel, configuration.getUtmConvert(), 100);

               if(!gameEngineModel.getGameUTMLocation().utm.getUtm().equals(gameEngineModel.getGameObject().utmLocation.utm.getUtm())
                       ||!gameEngineModel.getGameUTMLocation().subUtm.getUtm().equals(gameEngineModel.getGameObject().utmLocation.subUtm.getUtm())){
                   //we have changed....as we only register in sub utms...simply remove from current, add to new...does mean user gets shit from it but heyho.
                   hazelcastManagerInterface.unSubscribe(utm + gameEngineModel.getGameUTMLocation().subUtm.getUtm(), gameEngineModel.getGameObject().getTopicSubscriptions());
                   gameEngineModel.getGameObject().getTopicSubscriptions().addSubscription(gameEngineModel.getGameUTMLocation().utm.getUtm() + gameEngineModel.getGameUTMLocation().subUtm.getUtm(),
                           hazelcastManagerInterface.subscribe(gameEngineModel.getGameUTMLocation().utm.getUtm()+gameEngineModel.getGameUTMLocation().subUtm.getUtm(), gameEngineModel.getPlayerKey()));
                   gameEngineModel.getGameObject().utmLocation = gameEngineModel.getGameUTMLocation();
                   removeGameEngineModel(gameEngineModel);
                   //we now need to add to our temp map...in order to sort out once all processed.  a list would of been better key pointless.
                   hazelcastManagerInterface.get(GAME_ENGINE_MOVE_MAP).put(gameEngineModel.getGameObject().getKey(), gameEngineModel);
               }else{
                   //can simply update our utm location..we would lose altitude etc but not using that anyway..or speed.
                   gameEngineModel.getGameObject().utmLocation = gameEngineModel.getGameUTMLocation();
               }

             }

        }

        IMap utmMap = hazelcastManagerInterface.get(GAME_ENGINE_MOVE_MAP);

         //anyway...its not great...but need to get our temp back...to think about it

        //next step.  put the GameEngineModel into its correct utm / sub utm using add obviously.....

        //now process collisions.  basically given a missile in a grid (thats all we care about) that has reached its destination we then check
        //for items within that range...

        //again: slight issue: we do not actually stop something going past its destination...lol. either way, its capped at same time step basically.
        //otherwise gets very complicated.

        //ie last step = same time.



    }


}
