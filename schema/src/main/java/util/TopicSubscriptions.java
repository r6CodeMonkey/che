package util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by timmytime on 15/01/16.
 */
public class TopicSubscriptions implements Serializable {


    private final Map<String, String> subscriptions;

    public TopicSubscriptions() {
        subscriptions = new HashMap<>();
    }

    public TopicSubscriptions(TopicSubscriptions topicSubscriptions){
        subscriptions = new HashMap<>();
        for(String key : topicSubscriptions.getKeySet()){
            subscriptions.put(key, topicSubscriptions.getSubscription(key));
        }
    }

    public String getSubscription(String topic) {
        return subscriptions.get(topic);
    }

    public void addSubscription(String topic, String subscription) {
        subscriptions.put(topic, subscription);
    }

    public void removeSubscription(String topic) {
        subscriptions.remove(topic);
    }

    public Set<String> getKeySet(){
        return subscriptions.keySet();
    }
}
