package model;

import org.json.JSONObject;
import util.Tags;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 15/01/16.
 */
public class Alliance extends CoreModel {

    public String name;
    private List<Player> members = new ArrayList<>();

    public Alliance(String key) {
        super(key);
    }

    public List<Player> getMembers() {
        return members;
    }

    @Override
    public String getMessage() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(Tags.ALLIANCE_KEY, key);
        jsonObject.put(Tags.ALLIANCE_NAME, name);
        jsonObject.put(Tags.ALLIANCE_MEMBERS, members);

        return jsonObject.toString();
    }
}
