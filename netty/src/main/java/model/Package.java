package model;

import message.in.InPackageMessage;
import util.StoreKey;

import java.io.Serializable;

/**
 * Created by timmytime on 10/02/15.
 */
public class Package implements Serializable {

    private InPackageMessage message;
    private StoreKey packageKey;

    public Package(InPackageMessage message) {
        this.message = message;
        this.setPackageKey("");
    }

    public void setPackageKey(String regID) {
        packageKey = new StoreKey(message.getPid(), regID);
    }

    public StoreKey getPackageKey() {
        return packageKey;
    }


}
