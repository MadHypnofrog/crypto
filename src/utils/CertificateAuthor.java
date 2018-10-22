package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CertificateAuthor {
    public static MessageDigest digester;
    public static final int VALIDITY = 1337;

    static {
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
