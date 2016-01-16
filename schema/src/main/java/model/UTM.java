package model;

/**
 * Created by timmytime on 15/01/16.
 */
public class UTM extends CoreModel {

    private String utmLat, utmLong;

    public UTM() {
        utmLat = "";
        utmLong = "";
    }

    public UTM(String key) {
        super(key);
    }

    @Override
    public String getMessage() {

        message.UTM utm = new message.UTM();
        utm.create();

        utm.setUTMGrid(getUtm());
        utm.setUTMGrid(getUtm());

        return utm.toString();
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
