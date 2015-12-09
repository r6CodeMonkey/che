package com.oddymobstar.server.netty.model;

import com.oddymobstar.server.netty.message.in.CoreMessage;
import com.oddymobstar.server.netty.util.StoreKey;
import com.oddymobstar.server.netty.util.UTM;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by timmytime on 10/02/15.
 */
public class Player implements Serializable {

    private StoreKey playerKey;

    private Map<String, StoreKey> packageKeys = new HashMap<>();
    private Map<String, StoreKey> allianceKeys = new HashMap<>();
    //we dont really want them in multiple grids, but it could happen if they request
    //information from grids they are not in....but they can receive messages on grids of interest
    private Map<String, StoreKey> gridKeys = new HashMap<>();

    private UTM currentUTM = new UTM("", "");
    private UTM currentSubUTM = new UTM("", "");
    private UTM previousUTM = new UTM("", "");
    private UTM previousSubUTM = new UTM("", "");

    private SocketAddress socketAddress;

    private String id;

    private double longitude;
    private double latitude;
    private double altitude;
    private double speed;

    private boolean genUUID = false;


    private String image;
    /*
      a player will also have identifications of alliance / grid / package topics
      and regIDs for each.  they dont register to their own channel tho.
     */

    public Player(CoreMessage coreMessage) {
        this.id = coreMessage.getUniqueIdentifier();
        this.speed = coreMessage.getSpeed();
        this.altitude = coreMessage.getAltitude();
        this.longitude = coreMessage.getLongitude();
        this.latitude = coreMessage.getLatitude();
        this.setPlayerKey("");
        genUUID = true;

    }

    public Player(String id) {
        this.id = id;
        this.setPlayerKey("");
    }


    public boolean isGenUUID() {
        return genUUID;
    }


    public void setPlayerKey(String regID) {
        playerKey = new StoreKey(id, regID);
    }

    public StoreKey getPlayerKey() {
        return playerKey;
    }

    public void addPackageKey(StoreKey packageKey) {
        packageKeys.put(packageKey.getStoreID(), packageKey);
    }

    public void addAllianceKey(StoreKey allianceKey) {
        allianceKeys.put(allianceKey.getStoreID(), allianceKey);
    }

    public void addGridKey(StoreKey gridKey) {
        gridKeys.put(gridKey.getStoreID(), gridKey);
    }

    public Map<String, StoreKey> getAllianceKeys() {
        return allianceKeys;
    }

    public void removePackageKey(StoreKey packageKey) {
        packageKeys.remove(packageKey.getStoreID());
    }

    public void removeGridKey(StoreKey gridKey) {
        gridKeys.remove(gridKey.getStoreID());
    }

    public void removeAllianceKey(StoreKey allianceKey) {
        allianceKeys.remove(allianceKey.getStoreID());
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public StoreKey getGridKey(String id) {
        return gridKeys.get(id);
    }

    public StoreKey getAllianceKey(String id) {
        return allianceKeys.get(id);
    }

    public StoreKey getPackageKey(String id) {
        return packageKeys.get(id);
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    //do both ie set and change indicator
    public boolean hasUTMChanged(UTM utm) {

        previousUTM = currentUTM;
        currentUTM = utm;

        return !previousUTM.getUtm().equals(currentUTM.getUtm());
    }

    public boolean hasSubUTMChanged(UTM subUTM) {

        previousSubUTM = currentSubUTM;
        currentSubUTM = subUTM;

        return !previousSubUTM.getUtm().equals(currentSubUTM.getUtm());
    }

    public UTM getCurrentUTM() {
        return currentUTM;
    }

    public UTM getCurrentSubUTM() {
        return currentSubUTM;
    }

    public UTM getPreviousUTM() {
        return previousUTM;
    }

    public UTM getPreviousSubUTM() {
        return previousSubUTM;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void updateLocation(CoreMessage coreMessage) {
        this.speed = coreMessage.getSpeed();
        this.altitude = coreMessage.getAltitude();
        this.longitude = coreMessage.getLongitude();
        this.latitude = coreMessage.getLatitude();
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

}
