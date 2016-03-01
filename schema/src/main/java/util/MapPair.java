package util;

import java.io.Serializable;

/**
 * Created by timmytime on 01/03/16.
 */
public class MapPair implements Serializable {

    private final String key;
    private final Object object;

    public MapPair(String key, Object object){
        this.key = key;
        this.object = object;
    }

    public String getKey(){
        return key;
    }

    public Object getObject(){
        return object;
    }
}
