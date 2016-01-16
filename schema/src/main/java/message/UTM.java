package message;

import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class UTM extends CoreMessage {

    public UTM() {
    }

    public UTM(String message) {
        super(message);
    }

    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        inner.put(Tags.UTM_LAT_GRID, "");
        inner.put(Tags.UTM_LONG_GRID, "");
        this.put(Tags.UTM, inner);

    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

    }

    public String getUTMLatGrid() {
        return this.getJSONObject(Tags.UTM).get(Tags.UTM_LAT_GRID).toString();
    }

    public String getUTMLongGrid() {
        return this.getJSONObject(Tags.UTM).get(Tags.UTM_LONG_GRID).toString();
    }

    public void setUTMLatGrid(String utmLatGrid) {
        this.getJSONObject(Tags.UTM).put(Tags.UTM_LAT_GRID, utmLatGrid);
    }

    public void setUTMLongGrid(String utmLongGrid) {
        this.getJSONObject(Tags.UTM).put(Tags.UTM_LONG_GRID, utmLongGrid);
    }



}
