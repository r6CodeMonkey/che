package util;

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
        return (MessageDigest.getInstance(algorithm).digest(("Player Unique ID" + UUID.randomUUID().toString() + System.currentTimeMillis()).toString().getBytes())).toString();
    }

}
