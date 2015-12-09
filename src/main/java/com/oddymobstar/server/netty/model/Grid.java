package com.oddymobstar.server.netty.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by timmytime on 10/02/15.
 */
public class Grid implements Serializable {

    /*
      A grid will be a sub grid.  The will be held agains the UTM grid.
      A sub grid can contain any of package / player
     */
    //we need to be able to remove items from these sub grids.
    //really we just need to know what is in a sub grid.


    private Map<String, Player> players = new HashMap<>();
    private Map<String, Package> packages = new HashMap<>();
    //topics can be players but we need to switch the player key with topic key.
    //needs more thought.  will break privacy.
    // private Map<StoreKey, Player> topics = new HashMap<StoreKey, Player>();

    /*
    we have work to do here.  ie send the user information of whats
    in the grid.

    they also only want new information.
    therefore, they get the current shit on enter, then only receive new information
    as it appears in the grid.  yeah simples.
     */


    private String key; //this the UTM subkey

    public Grid(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void addPlayer(Player player) {
        this.players.put(player.getPlayerKey().getStoreID(), player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getPlayerKey().getStoreID());
    }

    public void addPackage(Package pack) {
        this.packages.put(pack.getPackageKey().getStoreID(), pack);
    }

    public void removePackage(Package pack) {
        this.packages.remove(pack.getPackageKey().getStoreID());
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Map<String, Package> getPackages() {
        return packages;
    }
}
