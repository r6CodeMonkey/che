package message;

import model.CoreModel;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Tags;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 15/01/16.
 */
public class Alliance extends CoreMessage {

    public Alliance(){

    }

    public Alliance(String message) {
        super(message);
    }

    public Alliance(CoreModel model) {
        super(model.getMessage());
    }

    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        inner.put(Tags.ALLIANCE_KEY, "");
        inner.put(Tags.ALLIANCE_NAME, "");
        inner.put(Tags.VALUE, "");
        inner.put(Tags.STATE, "");
        List<Player> members = new ArrayList<>();
        inner.put(Tags.ALLIANCE_MEMBERS, members);
        this.put(Tags.ALLIANCE, inner);

    }

    @Override
    public String getKey() {
        return this.getJSONObject(Tags.ALLIANCE).get(Tags.ALLIANCE_KEY).toString();
    }

    public String getState(){
        return this.getJSONObject(Tags.ALLIANCE).get(Tags.STATE).toString();
    }

    public String getValue(){
        return this.getJSONObject(Tags.ALLIANCE).get(Tags.VALUE).toString();
    }

    public String getName(){
        return this.getJSONObject(Tags.ALLIANCE).get(Tags.ALLIANCE_NAME).toString();
    }

    public List<Player> getMembers(){

        JSONArray array = this.getJSONObject(Tags.ALLIANCE).getJSONArray(Tags.ALLIANCE_MEMBERS);

        List<Player> members = new ArrayList<>();

        for(int i=0;i<array.length();i++) {
            members.add(new Player(array.get(i).toString()));
        }

        return members;
    }

    @Override
    public void setKey(String key) {
        this.getJSONObject(Tags.ALLIANCE).put(Tags.ALLIANCE_KEY, key);
    }

    public void setName(String name){
        this.getJSONObject(Tags.ALLIANCE).put(Tags.ALLIANCE_NAME, name);
    }

    public void setState(String state){
        this.getJSONObject(Tags.ALLIANCE).put(Tags.STATE, state);
    }

    public void setValue(String value){
        this.getJSONObject(Tags.ALLIANCE).put(Tags.VALUE, value);
    }

    public void setMembers(List<Player> members){
        this.getJSONObject(Tags.ALLIANCE).put(Tags.ALLIANCE_MEMBERS, members);
    }



}
