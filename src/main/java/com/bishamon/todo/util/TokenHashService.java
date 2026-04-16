package com.bishamon.todo.util;

import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.exception.AppException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenHashService {
    @Value("${jwt.refresh-token-hash-pepper}")
    String pepper;
    static final String ALGORITHM = "HmacSHA256";

    public String hash(String token){
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(pepper.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            mac.init(keySpec);

            byte[] rawHashToken = mac.doFinal(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(rawHashToken);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_HASH_FAILED);
        }
    }

    public boolean matches(String rawToken, String storedHash) {
        String computedHash = hash(rawToken);
        return MessageDigest.isEqual(
                computedHash.getBytes(StandardCharsets.UTF_8),
                storedHash.getBytes(StandardCharsets.UTF_8)
        );
    }
}
