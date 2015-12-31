package model.server;

import java.io.Serializable;

/**
 * Created by timmytime on 17/02/15.
 */
public class UTM implements Serializable {
    private String utmLat;
    private String utmLong;

    public UTM() {
        utmLat = "";
        utmLong = "";
    }

    public UTM(String utmLat, String utmLong) {
        this.utmLat = utmLat;
        this.utmLong = utmLong;

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