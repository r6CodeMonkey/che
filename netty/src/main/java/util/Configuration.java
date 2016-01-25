package util;

import factory.CheChannelFactory;
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

    private static Logger logger = Logger.getLogger("che.netty");
    private double SUB_ZONE_LAT = 0.05;
    private double SUB_ZONE_LONG = 0.1;
    private UUIDGenerator uuidGenerator = new UUIDGenerator("MD5");
    private UTMConvert utmConvert = new UTMConvert(SUB_ZONE_LAT, SUB_ZONE_LONG);

    private int bossThreads = 1;
    private int workerThreads = 5;
    private int port = 8085;
    private int chePort = 8086;
    private String cheIP = "localhost";
    private String keyStore = "";
    private String keyStorePassword;
    private String keyStoreFormat = "jks";
    private String trustStore;
    private String trustStorePassword;
    private String trustStoreFormat = "jks";
    private String sslProtocol = "rsa";
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
        setChePort(Integer.valueOf(element.getAttribute("chePort")));
        setCheIP(element.getAttribute("cheIP"));
        setBossThreads(Integer.valueOf(element.getAttribute("boss")));
        setWorkerThreads(Integer.valueOf(element.getAttribute("worker")));
        setKeyStore(element.getAttribute("keystore"));
        setTrustStore(element.getAttribute("truststore"));
        setKeyStoreFormat(element.getAttribute("keystoreformat"));
        setTrustStoreFormat(element.getAttribute("truststoreformat"));
        setKeyStorePassword(element.getAttribute("keystorepass"));
        setTrustStorePassword(element.getAttribute("truststorepass"));
        setSslProtocol(element.getAttribute("sslprotocol"));

        //server options
        setNodeDelay(Boolean.valueOf(element.getAttribute("nodedelay")));
        setSoLinger(Integer.valueOf(element.getAttribute("solinger")));
        setSoTimeout(Integer.valueOf(element.getAttribute("sotimeout")));
        setBacklog(Integer.valueOf(element.getAttribute("backlog")));
        setKeepAlive(Boolean.valueOf(element.getAttribute("keepalive")));
        setSoRcvBuf(Integer.valueOf(element.getAttribute("sorcvbuf")));
        setSoSndBuf(Integer.valueOf(element.getAttribute("sosndbuf")));

        setEpollMode(Boolean.valueOf(element.getAttribute("epollmode")));

        uuidGenerator = new UUIDGenerator(String.valueOf(element.getAttribute("uuidalgo")));

        uuidGenerator = new UUIDGenerator(String.valueOf(element.getAttribute("uuidalgo")));
        utmConvert = new UTMConvert(Double.valueOf(element.getAttribute("subzonelat")), Double.valueOf(element.getAttribute("subzonelong")));

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

    public int getChePort() {
        return chePort;
    }

    public void setChePort(int chePort) {
        this.chePort = chePort;
    }

    public String getCheIP() {
        return cheIP;
    }

    public void setCheIP(String cheIP) {
        this.cheIP = cheIP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public String getSslProtocol() {
        return sslProtocol;
    }

    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getKeyStoreFormat() {
        return keyStoreFormat;
    }

    public void setKeyStoreFormat(String keyStoreFormat) {
        this.keyStoreFormat = keyStoreFormat;
    }

    public String getTrustStoreFormat() {
        return trustStoreFormat;
    }

    public void setTrustStoreFormat(String trustStoreFormat) {
        this.trustStoreFormat = trustStoreFormat;
    }

    public UUIDGenerator getUuidGenerator() {
        return uuidGenerator;
    }

    public UTMConvert getUtmConvert() {
        return utmConvert;
    }

}
