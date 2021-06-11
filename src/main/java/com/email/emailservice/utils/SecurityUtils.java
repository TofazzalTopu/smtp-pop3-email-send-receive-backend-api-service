package com.email.emailservice.utils;

import com.auth0.jwt.JWT;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * SecurityUtils Utitlity class for security things
 *
 * @author Tofazzal
 * @Date: 28th April 2021
 */
public final class SecurityUtils {

    private static String mpSecretKey = "MY_SECRET";

    private SecurityUtils() {
        throw new IllegalStateException("SecurityUtils is a Utility class. Instantiation is not allowed");
    }

    final public static <T> T getJwtClaim(final String token, String key, Class<T> claz) {
        return JWT.decode(token).getClaims().get(key).as(claz);
    }

    final public static boolean isAccessAllowd(String token, Long companyId, Long userId) {
        return SecurityUtils.<Long>getJwtClaim(token, "userId", Long.class).equals(userId)
                || SecurityUtils.<Long>getJwtClaim(token, "companyId", Long.class).equals(companyId);
    }

    public static final String encryptPassword(String password) {
        if(password == null || password.equals("")){
            return null;
        }
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(mpSecretKey);
        return encryptor.encrypt(password);
    }

    public static final String decryptPassword(String password) {
        if(password == null || password.equals("")){
            return null;
        }
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setPassword(mpSecretKey);
        return decryptor.decrypt(password);
    }
}
