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

    public String generateKey(String keyType) throws NoSuchAlgorithmException {
        return (MessageDigest.getInstance(algorithm).digest((keyType + UUID.randomUUID().toString() + System.currentTimeMillis()).toString().getBytes())).toString();
    }

}
