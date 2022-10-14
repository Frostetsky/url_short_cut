package ru.shortcut.app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Component
@Getter
public class SecretHelperConfigurator {


    public final static String TAG_AUTHORIZATION = "Authorization";

    private final String secret;
    private final int expDays;
    private final String tokenType;

    public SecretHelperConfigurator(Environment environment) {
        this.secret = environment.getProperty(requireNonNull(getByKey("secret")));
        this.expDays = Integer.parseInt(requireNonNull(environment.getProperty(getByKey("exp-token-time-days"))));
        this.tokenType = environment.getProperty(requireNonNull(getByKey("token-type")));
    }

    public String generateToken(String site, String ipAddress) {
        var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, expDays);
        var baseSecret = new String(Base64.getEncoder().encode(secret.concat(ipAddress).getBytes()));
        return JWT.create()
                .withSubject(site)
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC512(baseSecret));
    }

    public String generateLogin() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().concat(secret).getBytes()));
    }

    public String generatePassword() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().concat(secret).getBytes()));
    }

    public String generateCode() {
        return UUID.randomUUID().toString();
    }

    public String decodeTokenAndGetSite(String token, String ipAddress) {
        var baseSecret = new String(Base64.getEncoder().encode(secret.concat(ipAddress).getBytes()));
        return JWT.require(Algorithm.HMAC512(baseSecret))
                .build()
                .verify(token.replace(tokenType, EMPTY))
                .getSubject();
    }

    public boolean isExpired(String token, String ipAddress) {
        var baseSecret = new String(Base64.getEncoder().encode(secret.concat(ipAddress).getBytes()));
        return JWT.require(Algorithm.HMAC512(baseSecret))
                .build()
                .verify(token.replace(tokenType, EMPTY))
                .getExpiresAt().getTime() < System.currentTimeMillis();
    }

    private String getByKey(String key) {
        return String.format("credential.app.%s", key);
    }
}
