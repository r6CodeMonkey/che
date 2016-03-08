package util;

import org.apache.log4j.Logger;
import util.map.UTMConvert;

/**
 * Created by timmytime on 04/03/16.
 */
public class Configuration {

    private static Logger logger = Logger.getLogger("che.engine");
    private int port = 1098;
    private String url = "//127.0.0.1:1098/GameEngineServer";
    private int gameEngineDelta = 6000; //1 minute default
    private double SUB_ZONE_LAT = 0.05;
    private double SUB_ZONE_LONG = 0.1;
    private UTMConvert utmConvert = new UTMConvert(SUB_ZONE_LAT, SUB_ZONE_LONG);
    private int hazelcastPort = 1099;
    private String hazelcastURL = "//localhost/HazelcastServer";


    public Configuration() {


    }

    public Configuration(String path) { //really need 1 config in a file soon!

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

    public void setPort(int port) {
        this.port = port;
    }

    public String getURL() {
        return url;
    }

    public int getGameEngineDelta() {
        return gameEngineDelta;
    }

    public void setGameEngineDelta(int gameEngineDelta) {
        this.gameEngineDelta = gameEngineDelta;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHazelcastPort() {
        return hazelcastPort;
    }


    public String getHazelcastURL() {
        return hazelcastURL;
    }

}
