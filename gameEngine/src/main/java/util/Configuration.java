package util;

import util.map.UTMConvert;

import org.apache.log4j.Logger;

/**
 * Created by timmytime on 04/03/16.
 */
public class Configuration {

    private int port = 1097;
    private String url = "//localhost/GameEngineServer";
    private int gameEngineDelta = 60000; //1 minute default
    private double SUB_ZONE_LAT = 0.05;
    private double SUB_ZONE_LONG = 0.1;
    private UTMConvert utmConvert = new UTMConvert(SUB_ZONE_LAT, SUB_ZONE_LONG);
    private static Logger logger = Logger.getLogger("che.engine");
    private int hazelcastPort = 1099;
    private String hazelcastURL = "//localhost/HazelcastServer";



    public Configuration() {


    }

    public Configuration(String path) {

    }

    public UTMConvert getUtmConvert() {
        return utmConvert;
    }

    public Logger getLogger() {
        return logger;
    }



    public int getPort() {
        return port;
    }

    public String getURL() {
        return url;
    }

    public int getGameEngineDelta(){
        return gameEngineDelta;
    }

    public void setPort(int port){
        this.port = port;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setGameEngineDelta(int gameEngineDelta){this.gameEngineDelta = gameEngineDelta;}

    public int getHazelcastPort() {
        return hazelcastPort;
    }


    public String getHazelcastURL() {
        return hazelcastURL;
    }

}
