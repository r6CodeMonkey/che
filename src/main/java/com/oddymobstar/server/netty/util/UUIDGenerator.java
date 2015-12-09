package com.oddymobstar.server.netty.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by timmytime on 20/02/15.
 */
public final class UUIDGenerator {

    private String algorithm;

    public UUIDGenerator(String algorithm) {
        this.algorithm = algorithm;
    }


    public String generatePlayerKey() throws NoSuchAlgorithmException {
        return (MessageDigest.getInstance(algorithm).digest(("Player" + UUID.randomUUID().toString() + System.currentTimeMillis()).toString().getBytes())).toString();
    }

    public String generateAllianceKey() throws NoSuchAlgorithmException {
        return (MessageDigest.getInstance(algorithm).digest(("Alliance" + UUID.randomUUID().toString() + System.currentTimeMillis()).toString().getBytes())).toString();
    }

    public String generatePackageKey() throws NoSuchAlgorithmException {
        return (MessageDigest.getInstance(algorithm).digest(("Package" + UUID.randomUUID().toString() + System.currentTimeMillis()).toString().getBytes())).toString();
    }

    public String generateTopicKey() throws NoSuchAlgorithmException {
        return (MessageDigest.getInstance(algorithm).digest(("Topic" + UUID.randomUUID().toString() + System.currentTimeMillis()).toString().getBytes())).toString();
    }
}
