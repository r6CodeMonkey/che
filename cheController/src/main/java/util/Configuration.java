package util;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by timmytime on 09/12/15.
 */
public class Configuration {


    /* private UUIDGenerator uuidGenerator = new UUIDGenerator("MD5");
     private UTMConvert utmConvert = new UTMConvert(SUB_ZONE_LAT, SUB_ZONE_LONG);
 */
    private static Logger logger = Logger.getLogger("che.cheController");
    private int bossThreads = 1;
    private int workerThreads = 5;
    private int port = 8086;
    //server options
    private boolean nodeDelay = true;
    private int soLinger = 1000;
    private int soTimeout = 0;
    private int soSndBuf;
    private int soRcvBuf;
    private boolean keepAlive = true;
    private int backlog = 128;
    private boolean epollMode = false;
    private int hazelcastPort = 1099;
    private String hazelcastURL = "//localhost/HazelcastServer";

    private ChannelMapController channelMapController = new ChannelMapController();


    public Configuration() {

    }

    public Configuration(String filePath) throws SAXException, IOException, ParserConfigurationException {

        File config = new File(filePath);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
        Document doc = dbBuilder.parse(config);

        //we have 1 node.
        Element element = doc.getElementById("config");

        setPort(Integer.valueOf(element.getAttribute("port")));
        setBossThreads(Integer.valueOf(element.getAttribute("boss")));
        setWorkerThreads(Integer.valueOf(element.getAttribute("worker")));

        //server options
        setNodeDelay(Boolean.valueOf(element.getAttribute("nodedelay")));
        setSoLinger(Integer.valueOf(element.getAttribute("solinger")));
        setSoTimeout(Integer.valueOf(element.getAttribute("sotimeout")));
        setBacklog(Integer.valueOf(element.getAttribute("backlog")));
        setKeepAlive(Boolean.valueOf(element.getAttribute("keepalive")));
        setSoRcvBuf(Integer.valueOf(element.getAttribute("sorcvbuf")));
        setSoSndBuf(Integer.valueOf(element.getAttribute("sosndbuf")));

        setEpollMode(Boolean.valueOf(element.getAttribute("epollmode")));

    /*    uuidGenerator = new UUIDGenerator(String.valueOf(element.getAttribute("uuidalgo")));
        utmConvert = new UTMConvert(Double.valueOf(element.getAttribute("subzonelat")), Double.valueOf(element.getAttribute("subzonelong")));
*/

    }

    public Logger getLogger() {
        return logger;
    }

    public boolean getEpollMode() {
        return epollMode;
    }

    public void setEpollMode(boolean epollMode) {
        this.epollMode = epollMode;
    }

    public boolean getNodeDelay() {
        return nodeDelay;
    }

    public void setNodeDelay(boolean nodeDelay) {
        this.nodeDelay = nodeDelay;
    }

    public int getSoLinger() {
        return soLinger;
    }

    public void setSoLinger(int soLinger) {
        this.soLinger = soLinger;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getSoSndBuf() {
        return soSndBuf;
    }

    public void setSoSndBuf(int soSndBuf) {
        this.soSndBuf = soSndBuf;
    }

    public int getSoRcvBuf() {
        return soRcvBuf;
    }

    public void setSoRcvBuf(int soRcvBuf) {
        this.soRcvBuf = soRcvBuf;
    }

    public boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public int getHazelcastPort() {
        return hazelcastPort;
    }

    public void setHazelcastPort(int hazelcastPort) {
        this.hazelcastPort = hazelcastPort;
    }

    public String getHazelcastURL() {
        return hazelcastURL;
    }

    public void setHazelcastURL(String hazelcastURL) {
        this.hazelcastURL = hazelcastURL;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBossThreads() {
        return bossThreads;
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public ChannelMapController getChannelMapController() {
        return channelMapController;
    }
}


/**
 * public UUIDGenerator getUuidGenerator() {
 * return uuidGenerator;
 * }
 * public UTMConvert getUtmConvert() {
 * return utmConvert;
 * }
 **/