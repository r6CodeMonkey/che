package model;

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
    private List<String> alliances = new ArrayList<>();  //just hold the keys.
    private List<Missile> missiles = new ArrayList<>();
    private List<GameObject> gameObjects = new ArrayList<>();
    private TopicSubscriptions topicSubscriptions = new TopicSubscriptions();

    public Player(String key) {
        super(key);
        utmLocation = new UTMLocation();
    }

    public Player(message.Player player) {
        super(player.getKey());
        name = player.getName();
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

    public List<Missile> getMissiles() {
        return missiles;
    }

    public List<GameObject> gameObjects() {
        return gameObjects;
    }


    @Override
    public String getMessage() {

        message.Player player = new message.Player();
        player.create();

        player.setKey(key);
        player.setName(name);
        player.setImage("");   //to fix
        player.setUTMLocation(new message.UTMLocation(utmLocation.getMessage()));

        return player.toString();
    }
}
