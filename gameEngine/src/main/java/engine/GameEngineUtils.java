package engine;

import core.HazelcastManagerInterface;
import model.GameEngineModel;
import util.Configuration;
import util.TopicPair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by timmytime on 01/03/16.
 */
public class GameEngineUtils {

    private final HazelcastManagerInterface hazelcastManagerInterface;
    private final Configuration configuration;

    public GameEngineUtils(HazelcastManagerInterface hazelcastManagerInterface, Configuration configuration) {
        this.hazelcastManagerInterface = hazelcastManagerInterface;
        this.configuration = configuration;
    }

    public List<String> getAvailableKeys(String utm) throws RemoteException {
        return (List<String>) hazelcastManagerInterface.getAvailableKeys(utm);
    }

    public List<GameEngineModel> getGameEngineModels(String utm, String subUtm) throws RemoteException {
        return (List<GameEngineModel>) hazelcastManagerInterface.get(utm, subUtm);
    }

    public void bulkSubscribe(List<GameEngineModel> gameEngineModels) {

        new Thread(() -> {
            try {
                hazelcastManagerInterface.bulkSubscribe(gameEngineModels.stream().map(model -> new TopicPair(model.getPlayerKey(),
                        model.getGameUTMLocation().utm.getUtm() + model.getGameUTMLocation().subUtm.getUtm(),
                        model.getGameObject().getTopicSubscriptions(), model.getMessage())).collect(Collectors.toList()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }).start();
    }


    public void bulkUnSubscribe(String utm, String subUtm, List<GameEngineModel> gameEngineModels) {

        new Thread(() -> {
            try {
                hazelcastManagerInterface.bulkUnSubscribe(gameEngineModels.stream().map(model -> new TopicPair(model.getPlayerKey(),
                        utm + subUtm,
                        model.getGameObject().getTopicSubscriptions(), model.getMessage2())).collect(Collectors.toList()));

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void updateSubUTM(String utm, String subUtm, List<GameEngineModel> gameEngineModels) throws RemoteException {
        hazelcastManagerInterface.put(utm, subUtm, gameEngineModels);
    }

    public void addToSubUTM(String utm, String subUtm, List<GameEngineModel> gameEngineModels) throws RemoteException {
        List<GameEngineModel> subUtmList = (List<GameEngineModel>) hazelcastManagerInterface
                .get(utm, subUtm);

        if (subUtmList != null && subUtmList.size() > 0) {
            subUtmList.addAll(gameEngineModels.stream().collect(Collectors.toList()));
            hazelcastManagerInterface.put(utm, subUtm, subUtmList);
        } else {
            updateSubUTM(utm, subUtm, gameEngineModels);
        }
    }

    public void bulkRemoveGameEngineModel(String utmKey, String subUtmKey, List<GameEngineModel> gameEngineModels) throws RemoteException {

        try {
            List<GameEngineModel> subUtmList = (List<GameEngineModel>) hazelcastManagerInterface.get(utmKey, subUtmKey);
            gameEngineModels.forEach(subUtmList::remove);
            hazelcastManagerInterface.put(utmKey, subUtmKey, subUtmList);

        } catch (Exception e) {
            configuration.getLogger().debug("remove game engine failed as perhaps it was not in the list " + e.getMessage());
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

    public void removeGameEngineModel(GameEngineModel gameEngineModel) throws RemoteException {      //is an assumption it exists..so maybe dont check?
        try {
            List<GameEngineModel> subUtmList = (List<GameEngineModel>) hazelcastManagerInterface.get(gameEngineModel.getGameObject().utmLocation.utm.getUtm(), gameEngineModel.getGameObject().utmLocation.subUtm.getUtm());
            subUtmList.remove(gameEngineModel);
            hazelcastManagerInterface.put(gameEngineModel.getGameObject().utmLocation.utm.getUtm(), gameEngineModel.getGameObject().utmLocation.subUtm.getUtm(), subUtmList);

        } catch (Exception e) {
            configuration.getLogger().debug("remove game engine failed as perhaps it was not in the list " + e.getMessage());
        }

    }

}
