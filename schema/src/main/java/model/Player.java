package model;

import util.TopicSubscriptions;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by timmytime on 15/01/16.
 */
public class Player extends CoreModel {

    public String name, value, state;
    public Image image;
    public UTMLocation utmLocation;
    private List<String> alliances = new ArrayList<>();  //just hold the keys.
    private Map<String, Missile> missiles = new HashMap<>();
    private Map<String, GameObject> gameObjects = new HashMap<>();
    private TopicSubscriptions topicSubscriptions = new TopicSubscriptions();

    public Player(String key) {
        super(key);
        utmLocation = new UTMLocation();
    }

    public Player(message.Player player) {
        super(player.getKey());
        name = player.getName();
        value = player.getValue();
        state = player.getState();

        //image = player.getImage().toString();
        utmLocation = new UTMLocation(player.getUTMLocation());
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

    public List<String> getAlliances() {
        return alliances;
    }

    public Map<String, Missile> getMissiles() {
        return missiles;
    }

    public Map<String, GameObject> getGameObjects() {
        return gameObjects;
    }


    @Override
    public String getMessage() {

        message.Player player = new message.Player();
        player.create();

        player.setKey(key);
        player.setName(name);
        player.setValue(value);
        player.setState(state);
        player.setImage("");   //to fix
        player.setUTMLocation(new message.UTMLocation(utmLocation.getMessage()));

        return player.toString();
    }
}
