package model;

import org.json.JSONObject;
import util.Tags;
import util.TopicSubscriptions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 15/01/16.
 */
public class Player extends CoreModel {

    public String name;
    public Image image;
    public UTMLocation utmLocation;
    private List<Alliance> alliances = new ArrayList<>();
    private List<Missile> missiles = new ArrayList<>();
    private List<GameObject> gameObjects = new ArrayList<>();
    private TopicSubscriptions topicSubscriptions = new TopicSubscriptions();

    public Player(String key) {
        super(key);
    }

    public TopicSubscriptions getTopicSubscriptions() {
        return topicSubscriptions;
    }

    public boolean hasUTMChanged(UTMLocation utmLocation) {
        return !this.utmLocation.utm.getUtm().equals(utmLocation.utm.getUtm());
    }

    public boolean hasSubUTMChanged(UTMLocation utmLocation) {
        return !this.utmLocation.subUtm.getUtm().equals(utmLocation.subUtm.getUtm());
    }

    public List<Alliance> getAlliances() {
        return alliances;
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    public List<GameObject> gameObjects() {
        return gameObjects;
    }


    @Override
    public String getMessage() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(Tags.PLAYER_KEY, key);
        jsonObject.put(Tags.PLAYER_NAME, name);
        jsonObject.put(Tags.PLAYER_IMAGE, image); //needs to be a stream base64..to do

        return jsonObject.toString();
    }
}
