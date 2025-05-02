package mate.academy.util;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {

    public static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    public static String generateSalt() {
        return BCrypt.gensalt();
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
