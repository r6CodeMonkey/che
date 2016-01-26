package message;

import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class Player extends CoreMessage {

    public Player() {
        super(Tags.PLAYER);
    }

    public Player(String message) {
        super(Tags.PLAYER, message);
    }


    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        inner.put(Tags.PLAYER_KEY, "");
        inner.put(Tags.PLAYER_NAME, "");
        inner.put(Tags.PLAYER_IMAGE, "");
        UTMLocation utmLocation = new UTMLocation();
        utmLocation.create();
        inner.put(Tags.UTM_LOCATION, utmLocation);
        this.put(Tags.PLAYER, inner);

    }


    @Override
    public String getKey() {
        return this.getJSONObject(Tags.PLAYER).get(Tags.PLAYER_KEY).toString();
    }

    @Override
    public void setKey(String key) {
        this.getJSONObject(Tags.PLAYER).put(Tags.PLAYER_KEY, key);
    }

    public String getName() {
        return this.getJSONObject(Tags.PLAYER).get(Tags.PLAYER_NAME).toString();
    }

    public void setName(String name) {
        this.getJSONObject(Tags.PLAYER).put(Tags.PLAYER_NAME, name);

    }

    public String getImage() {
        return this.getJSONObject(Tags.PLAYER).get(Tags.PLAYER_IMAGE).toString();
    }

    public void setImage(String image) {
        this.getJSONObject(Tags.PLAYER).put(Tags.PLAYER_IMAGE, image);

    }

    public UTMLocation getUTMLocation() {
        return new UTMLocation(this.getJSONObject(Tags.PLAYER).getJSONObject(Tags.UTM_LOCATION).toString());
    }

    public void setUTMLocation(UTMLocation utmLocation) {
        this.getJSONObject(Tags.PLAYER).put(Tags.UTM_LOCATION, utmLocation);
    }


}
