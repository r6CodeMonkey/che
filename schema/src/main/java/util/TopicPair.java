package util;

import message.HazelcastMessage;

import java.io.Serializable;

/**
 * Created by timmytime on 01/03/16.
 */
public class TopicPair implements Serializable {

    private final String topicKey;
    private final String key;
    private final String ownerKey;
    private final String message;

    public TopicPair(String key, String ownerKey, String topicKey) {
        this.key = key;
        this.ownerKey = ownerKey;
        this.topicKey = topicKey;
        this.message = null;
    }

    public TopicPair(String key, String ownerKey,  String topicKey, HazelcastMessage message) {
        this.key = key;
        this.ownerKey = ownerKey;
        this.topicKey = topicKey;
        this.message = message.toString();
    }

    public String getKey() {
        return key;
    }

    public String getTopicKey() {
        return topicKey;
    }

    public String getOwnerKey(){return ownerKey;}

    public String getMessage() {
        return message;
    }


    @Override
    public boolean equals(Object object) {
        if (((TopicPair) object).getKey().equals(this.key)
                && ((TopicPair) object).getTopicKey().equals(this.topicKey)
                && ((TopicPair) object).getOwnerKey().equals(this.ownerKey)) {
            return true;
        }

        return false;
    }

}
