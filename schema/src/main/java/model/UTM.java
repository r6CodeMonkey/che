package model;

/**
 * Created by timmytime on 15/01/16.
 */
public class UTM extends CoreModel {

    private String utmLat, utmLong;

    public UTM(){
        utmLat = "";
        utmLong = "";
    }

    public UTM(String key) {
        super(key);
    }

    @Override
    public String getMessage() {
        return null;
    }

    public String getUtmLat() {
        return utmLat;
    }

    public String getUtmLong() {
        return utmLong;
    }

    public String getUtm() {
        return utmLat + utmLong;
    }


}
