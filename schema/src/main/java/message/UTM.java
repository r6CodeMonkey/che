package message;

import model.CoreModel;
import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class UTM extends CoreMessage {

    public UTM(){}

    public UTM(String message) {
        super(message);
    }

    public UTM(CoreModel model) {
        super(model.getMessage());
    }

    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        inner.put(Tags.UTM_GRID, "");
        inner.put(Tags.SUB_UTM_GRID, "");
        this.put(Tags.UTM, inner);

    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

    }

    public void setUTMGrid(String utmGrid){
        this.getJSONObject(Tags.UTM).put(Tags.UTM_GRID, utmGrid);
    }

    public void setSubUTMGrid(String subUTMGrid){
        this.getJSONObject(Tags.UTM).put(Tags.SUB_UTM_GRID, subUTMGrid);
    }

    public String getUTMGrid(){
        return this.getJSONObject(Tags.UTM).get(Tags.UTM_GRID).toString();
    }

    public String getSubUTMGrid(){
        return this.getJSONObject(Tags.UTM).get(Tags.SUB_UTM_GRID).toString();
    }



}
