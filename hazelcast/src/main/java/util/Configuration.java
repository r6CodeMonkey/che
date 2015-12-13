package util;

/**
 * Created by timmytime on 13/12/15.
 */
public class Configuration {

    private int port = 1099;
    private String url = "//localhost/HazelcastServer";

    public Configuration(){

    }

    public Configuration(String path){

    }

    public int getPort(){
        return port;
    }

    public String getURL(){
        return url;
    }
}
