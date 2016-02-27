package model;

import util.GameObjectRules;

import java.io.Serializable;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEngineModel implements Serializable {


    private String playerKey;
    private GameObject gameObject;
    private GameObjectRules gameObjectRules;
    private UTMLocation gameUTMLocation = new UTMLocation();

    public GameEngineModel(String playerKey, GameObject gameObject, GameObjectRules gameObjectRules) {
        this.playerKey = playerKey;
        this.gameObject = gameObject;
        this.gameObjectRules = gameObjectRules;
    }

    public String getPlayerKey() {
        return playerKey;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public GameObjectRules getGameObjectRules() {
        return gameObjectRules;
    }

    public UTMLocation getGameUTMLocation() {
        return gameUTMLocation;
    }

    public boolean hasChangedGrid(){
        return !gameUTMLocation.utm.getUtm().equals(gameObject.utmLocation.utm.getUtm())
                ||gameUTMLocation.subUtm.getUtm().equals(gameObject.utmLocation.subUtm.getUtm());
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

