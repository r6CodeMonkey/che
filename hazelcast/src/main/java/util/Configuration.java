package util;

import com.hazelcast.config.Config;

/**
 * Created by timmytime on 13/12/15.
 */
public class Configuration {

    private int port = 1099;
    private String url = "//localhost/HazelcastServer";

    private static Config config;

    public Configuration() {


    }

    public static Config getConfig(){

        config = new Config();

        config.setProperty("hazelcast.event.thread.count", "500");
        return config;
    }

    public Configuration(String path) {
    }

    public int getPort() {
        return port;
    }

    public String getURL() {
        return url;
    }
}
