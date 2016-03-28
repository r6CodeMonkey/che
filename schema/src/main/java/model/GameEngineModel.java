package model;

import message.HazelcastMessage;
import util.GameObjectRules;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEngineModel implements Serializable {


    private String playerKey;
    private String playerRemoteAddress;
    private GameObject gameObject;
    private GameObjectRules gameObjectRules;
    private UTMLocation gameUTMLocation = new UTMLocation();
    private HazelcastMessage message;
    private HazelcastMessage message2;  //as in leavae or enter.  message 1 also destroyed etc.
    private boolean isMissile = false;

    private Map<UTM, List<UTM>> missileTargetGrids;


    public GameEngineModel(String playerKey, String playerRemoteAddress, GameObject gameObject, GameObjectRules gameObjectRules) {
        this.playerKey = playerKey;
        this.playerRemoteAddress = playerRemoteAddress;
        this.gameObject = gameObject;
        this.gameObjectRules = gameObjectRules;
    }

    public GameEngineModel(String playerKey, String playerRemoteAddress, GameObject gameObject, GameObjectRules gameObjectRules, boolean isMissile) {
        this.playerKey = playerKey;
        this.playerRemoteAddress = playerRemoteAddress;
        this.gameObject = gameObject;
        this.gameObjectRules = gameObjectRules;
        this.isMissile = isMissile;
    }



    public String getPlayerKey() {
        return playerKey;
    }

    public Map<UTM, List<UTM>> getMissileTargetGrids(){return missileTargetGrids;};

    public String getPlayerRemoteAddress() {
        return playerRemoteAddress;
    }

    public void setPlayerRemoteAddress(String playerRemoteAddress) {
        this.playerRemoteAddress = playerRemoteAddress;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public boolean isMissile(){
        return isMissile;
    }

    public void setMissileTargetGrids(Map<UTM, List<UTM>> missileTargetGrids){
        this.missileTargetGrids = missileTargetGrids;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObjectRules getGameObjectRules() {
        return gameObjectRules;
    }

    public UTMLocation getGameUTMLocation() {
        return gameUTMLocation;
    }

    public void setGameUTMLocation(UTMLocation utmLocation){
        this.gameUTMLocation = utmLocation;
    }

    public HazelcastMessage getMessage() {
        return message;
    }

    public void setMessage(HazelcastMessage message) {
        this.message = message;
    }

    public HazelcastMessage getMessage2() {
        return message2;
    }

    public void setMessage2(HazelcastMessage message2) {
        this.message2 = message2;
    }


    public String getUTMKey(){
        return gameObject.utmLocation.utm.getUtm()+gameObject.utmLocation.subUtm.getUtm();
    }

    public boolean hasChangedGrid() {


     return !gameUTMLocation.utm.getUtm().equals(gameObject.utmLocation.utm.getUtm())
                || !gameUTMLocation.subUtm.getUtm().equals(gameObject.utmLocation.subUtm.getUtm());
    }

    @Override
    public boolean equals(Object object) {
           //do rest later etc..plus hashcode bla bla
           if (((GameEngineModel) object).getGameObject().getKey().equals(this.gameObject.getKey())) {
               return true;
           }

        return false;
    }
}

