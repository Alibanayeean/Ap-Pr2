package server;

import java.security.SecureRandom;

public class AuthTokenGenerator {
    private static SecureRandom random = new SecureRandom();

    public static String nextToken() {
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String token = bytes.toString();
        return token;
    }
}