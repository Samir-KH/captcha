package ma.m2m.captcha;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class TokenHashing {

    private TokenHashing(){}

    public static String generateHash(String requestToken, String secretKey) {
        String sha256hex = Hashing.sha256()
                .hashString(requestToken + secretKey, StandardCharsets.UTF_8)
                .toString();
        return sha256hex;
    }
}
