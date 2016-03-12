package util;

import com.hazelcast.config.Config;
import org.apache.log4j.Logger;

/**
 * Created by timmytime on 13/12/15.
 */
public class Configuration {

    private static Config config;
    private static Logger logger = Logger.getLogger("che.hazelcast");

    private int port = 1099;
    private String url = "//localhost/HazelcastServer";

    public Configuration() {


    }

    public Configuration(String path) {
    }

    public static Config getConfig() {

        config = new Config();

        config.setProperty("hazelcast.event.thread.count", "500");
        return config;
    }

    public int getPort() {
        return port;
    }

    public String getURL() {
        return url;
    }

    public Logger getLogger(){return logger;}
}
