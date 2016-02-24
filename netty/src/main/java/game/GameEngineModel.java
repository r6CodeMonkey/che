package game;

import model.GameObject;
import model.UTMLocation;
import util.GameObjectRules;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEngineModel {


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

    @Override
    public boolean equals(Object object) {
        //do rest later etc..plus hashcode bla bla
        if (((GameEngineModel) object).playerKey.equals(this.playerKey)) {
            return true;
        }

        return false;
    }
}

