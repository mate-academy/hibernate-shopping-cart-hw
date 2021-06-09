package mate.academy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtil {
    public static final String HASH_ALGORITHM = "SHA-512";
    private static final int size = 16;

    public static String hashPassword(String password, byte[] salt) {
        StringBuilder hashedPwd = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.update(salt);
            byte[] digest = messageDigest.digest(password.getBytes());
            for (byte b : digest) {
                hashedPwd.append(String.format("%02x", b));
            }
            return hashedPwd.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could create hash using " + HASH_ALGORITHM
                    + " algorithm", e);
        }
    }

    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[size];
        random.nextBytes(salt);
        return salt;
    }
}
