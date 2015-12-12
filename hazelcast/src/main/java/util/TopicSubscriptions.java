package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by timmytime on 12/12/15.
 */
public class TopicSubscriptions {

    private final Map<String, String> subscriptions;

    public TopicSubscriptions() {
        subscriptions = new HashMap<>();
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

}
