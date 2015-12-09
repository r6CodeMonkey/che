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
 * Created by timmytime on 08/02/15.
 */
public class Configuration {


    private int bossThreads = 1;
    private int workerThreads = 5;

    private int port = 8085;

    private String keyStore;
    private String keyStorePassword;
    private String keyStoreFormat;

    private String trustStore;
    private String trustStorePassword;
    private String trustStoreFormat;

    private String sslProtocol;

    //server options
    private boolean nodeDelay = true;
    private int soLinger = 1000;
    private int soTimeout = 0;
    private int soSndBuf;
    private int soRcvBuf;
    private boolean keepAlive = true;
    private int backlog = 128;

    private double SUB_ZONE_LAT = 0.05;
    private double SUB_ZONE_LONG = 0.1;

    private boolean epollMode = false;

    private UUIDGenerator uuidGenerator = new UUIDGenerator("MD5");
    private UTMConvert utmConvert = new UTMConvert(SUB_ZONE_LAT, SUB_ZONE_LONG);

    private static Logger logger = Logger.getLogger("com.oddymobstar.server.netty");


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

        utmConvert = new UTMConvert(Double.valueOf(element.getAttribute("subzonelat")), Double.valueOf(element.getAttribute("subzonelong")));


    }

    public Logger getLogger() {
        return logger;
    }


    public void setEpollMode(boolean epollMode) {
        this.epollMode = epollMode;
    }

    public boolean getEpollMode() {
        return epollMode;
    }

    public void setNodeDelay(boolean nodeDelay) {
        this.nodeDelay = nodeDelay;
    }

    public boolean getNodeDelay() {
        return nodeDelay;
    }

    public void setSoLinger(int soLinger) {
        this.soLinger = soLinger;
    }

    public int getSoLinger() {
        return soLinger;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoSndBuf(int soSndBuf) {
        this.soSndBuf = soSndBuf;
    }

    public int getSoSndBuf() {
        return soSndBuf;
    }

    public void setSoRcvBuf(int soRcvBuf) {
        this.soRcvBuf = soRcvBuf;
    }

    public int getSoRcvBuf() {
        return soRcvBuf;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean getKeepAlive() {
        return keepAlive;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public void setKeyStoreFormat(String keyStoreFormat) {
        this.keyStoreFormat = keyStoreFormat;
    }

    public void setTrustStoreFormat(String trustStoreFormat) {
        this.trustStoreFormat = trustStoreFormat;
    }

    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    public int getPort() {
        return port;
    }

    public int getBossThreads() {
        return bossThreads;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public String getKeyStoreFormat() {
        return keyStoreFormat;
    }

    public String getTrustStoreFormat() {
        return trustStoreFormat;
    }

    public UUIDGenerator getUuidGenerator() {
        return uuidGenerator;
    }

    public UTMConvert getUtmConvert() {
        return utmConvert;
    }


}
