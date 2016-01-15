package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 15/01/16.
 */
public class Alliance extends CoreModel {

    private String name;
    private List<Player> members = new ArrayList<>();

    public Alliance(String key) {
        super(key);
    }

    public List<Player> getMembers(){
        return members;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
