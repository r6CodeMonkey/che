package model;

import util.StoreKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by timmytime on 10/02/15.
 */
public class Alliance implements Serializable {

    private StoreKey allianceKey;
    private Map<String, StoreKey> playerKeys = new HashMap<>();

    public Alliance(String aid) {
        setAllianceKey(aid, "");
    }


    public StoreKey getAllianceKey() {
        return allianceKey;
    }


    public void setAllianceKey(String aid, String regID) {
        allianceKey = new StoreKey(aid, regID);
    }

    public void addPlayer(Player player) {
        playerKeys.put(player.getPlayerKey().getStoreID(), player.getPlayerKey());
    }

    public void removePlayer(Player player) {
        playerKeys.remove(player.getPlayerKey().getStoreID());
    }

    public Map<String, StoreKey> getPlayerKeys() {
        return playerKeys;
    }

}
