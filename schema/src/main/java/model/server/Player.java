package model.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 31/12/15.
 */
public class Player implements Serializable {

    public String uid;
    public UTMLocation utmLocation = new UTMLocation();

    public List<Alliance> alliances = new ArrayList<>();
    public List<GameObject> gameObjects = new ArrayList<>();
    public List<Missile> missiles = new ArrayList<>();

    //need the topic subscriptions.
    private TopicSubscriptions topicSubscriptions = new TopicSubscriptions();

    public Player(String uid) {
        this.uid = uid;
    }

    public boolean hasUTMChanged(UTMLocation utmLocation) {
        return !this.utmLocation.utm.getUtm().equals(utmLocation.utm.getUtm());
    }

    public boolean hasSubUTMChanged(UTMLocation utmLocation) {
        return !this.utmLocation.subUtm.getUtm().equals(utmLocation.subUtm.getUtm());
    }

    public TopicSubscriptions getTopicSubscriptions() {
        return topicSubscriptions;
    }
}
