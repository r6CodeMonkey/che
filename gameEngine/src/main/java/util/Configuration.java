package util;

/**
 * Created by timmytime on 04/03/16.
 */
public class Configuration {

    private int port = 1099;
    private String url = "//localhost/HazelcastServer";

    public Configuration() {


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
