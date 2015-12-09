package util;

import java.io.Serializable;

/**
 * Created by timmytime on 10/02/15.
 */
public class StoreKey implements Serializable {

    private String regID = "";
    private String storeID = "";

    public StoreKey(String storeID, String regID) {
        this.regID = regID;
        this.storeID = storeID;
    }

    public String getRegID() {
        return regID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

}
