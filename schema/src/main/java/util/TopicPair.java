package util;

import java.io.Serializable;

/**
 * Created by timmytime on 01/03/16.
 */
public class TopicPair implements Serializable {

    private final TopicSubscriptions topicSubscriptions;
    private final String topicKey;
    private final String key;

    public TopicPair(String key, String topicKey, TopicSubscriptions topicSubscriptions){
        this.key = key;
        this.topicKey = topicKey;
        this.topicSubscriptions = topicSubscriptions;
    }

    public String getKey(){
        return key;
    }

    public String getTopicKey(){
        return topicKey;
    }

    public TopicSubscriptions getTopicSubscriptions(){
        return topicSubscriptions;
    }

    @Override
    public boolean equals(Object object) {
        if(((TopicPair)object).getKey().equals(this.key) &&  ((TopicPair)object).getTopicKey().equals(this.topicKey)){
            return true;
        }

        return false;
    }

    }
