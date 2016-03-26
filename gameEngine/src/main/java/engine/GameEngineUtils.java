package engine;

import core.HazelcastManagerInterface;
import message.HazelcastMessage;
import model.GameEngineModel;
import model.GameObject;
import model.Player;
import model.UTMLocation;
import org.json.JSONException;
import org.json.JSONObject;
import util.Configuration;
import util.Tags;
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

        List<GameEngineModel> temp = (List<GameEngineModel>) hazelcastManagerInterface.get(utm, subUtm);

        return temp.parallelStream().filter(gameEngineModel -> gameEngineModel.getGameObject().getDistanceBetweenPoints() != 0).collect(Collectors.toList());
    }

    public void bulkSubscribe(List<GameEngineModel> gameEngineModels) throws RemoteException {

        List<TopicPair> topicPairs = gameEngineModels.stream().map(model -> new TopicPair(model.getPlayerKey(), model.getGameObject().getKey(),
                model.getGameUTMLocation().utm.getUtm() + model.getGameUTMLocation().subUtm.getUtm(),model.getMessage())).collect(Collectors.toList());


        new Thread(() -> {
            try {
                hazelcastManagerInterface.bulkSubscribe(topicPairs);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }).start();
    }


    public void bulkUnSubscribe(String utm, String subUtm, List<GameEngineModel> gameEngineModels) throws RemoteException {


        List<TopicPair> topicPairs = gameEngineModels.stream().map(model -> new TopicPair(model.getPlayerKey(),model.getGameObject().getKey(),
                utm + subUtm, model.getMessage2())).collect(Collectors.toList());


        new Thread(() -> {
            try {
                hazelcastManagerInterface.bulkUnSubscribe(topicPairs);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void bulkPublish(String topic, List<GameEngineModel> gameEngineModels) throws RemoteException {
        configuration.getLogger().debug("bulk publish");
        hazelcastManagerInterface.bulkPublish(topic, gameEngineModels.stream().map(model -> model.getMessage().toString()).collect(Collectors.toList()));
    }

    public void updateSubUTM(String utm, String subUtm, List<GameEngineModel> gameEngineModels) throws RemoteException {
        hazelcastManagerInterface.put(utm, subUtm, gameEngineModels);
    }

    public void addToSubUTM(String utm, String subUtm, List<GameEngineModel> gameEngineModels) throws RemoteException {

        configuration.getLogger().debug("adding to "+utm+" / "+subUtm);
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

    public void processMoveMessage(GameEngineModel gameEngineModel) throws JSONException {
        gameEngineModel.getGameObject().state = Tags.MESSAGE;
        if(gameEngineModel.isMissile()){
            gameEngineModel.getGameObject().value = 0 == gameEngineModel.getGameObject().getDistanceBetweenPoints() ? Tags.MISSILE_DESTROYED : Tags.MISSILE_LAUNCHED;
        }else {
            gameEngineModel.getGameObject().value = 0 == gameEngineModel.getGameObject().getDistanceBetweenPoints() ? Tags.GAME_OBJECT_IS_FIXED : Tags.GAME_OBJECT_IS_MOVING;
        }

        GameObject temp;

        if (gameEngineModel.hasChangedGrid()) {
            gameEngineModel.getGameObject().utmLocation.state = Tags.MESSAGE;
            gameEngineModel.getGameObject().utmLocation.value = Tags.GAME_OBJECT_LEFT;

            temp = new GameObject(gameEngineModel.getGameObject());
            gameEngineModel.setMessage2(new HazelcastMessage(gameEngineModel.getPlayerRemoteAddress(),
                    new JSONObject(temp.utmLocation.getMessage())));

            temp.utmLocation = new UTMLocation(gameEngineModel.getGameUTMLocation());

            gameEngineModel.setMessage(new HazelcastMessage(gameEngineModel.getPlayerRemoteAddress(),
                    true,
                    new JSONObject().put(Tags.GAME_OBJECT, new message.GameObject(temp.getMessage()))));

        } else {
            //and now we update our location.
            temp = new GameObject(gameEngineModel.getGameObject());
            temp.utmLocation = new UTMLocation(gameEngineModel.getGameUTMLocation());

            gameEngineModel.setMessage(new HazelcastMessage(gameEngineModel.getPlayerRemoteAddress(),
                    true,
                    new JSONObject().put(Tags.GAME_OBJECT, new message.GameObject(temp.getMessage()))));

        }

        //finally update our model.  still not working
        gameEngineModel.setGameObject(temp);

    }


    public void updatePlayer(GameEngineModel gameEngineModel) throws RemoteException {

        Player player = (Player) hazelcastManagerInterface.get(Tags.PLAYER_MAP, gameEngineModel.getPlayerKey());
        player.getGameObjects().put(gameEngineModel.getGameObject().getKey(), gameEngineModel.getGameObject());
        hazelcastManagerInterface.put(Tags.PLAYER_MAP, player.getKey(), player);

    }

    //need a bulk variant ideally....
    //public void bulkUpdatePlayer(List<Gam>)

}
