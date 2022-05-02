package com.zuzex.vvolkov.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zuzex.vvolkov.exceptions.InvalidTokenException;
import com.zuzex.vvolkov.model.user.Role;
import com.zuzex.vvolkov.service.UserService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class TokenUtils {

    public static final TokenUtils instance = new TokenUtils();

    private TokenUtils() {

    }

    public static TokenUtils getInstance() {
        syncCommands = setUpRedis();
        return instance;
    }

    private static String redisHost = "localhost";
    private static int redisPort = 6379;
    private static String redisPassword = "sXe--123";
    private static int redisDatabase = 0;
    private static String jwtSecret = "guitar-secret-key";

    @Value("${token.access.expr.ms}")
    private final int accessTokenExp = 300000; // ms
    @Value("${token.refresh.expr.sec}")
    private final int refreshTokenExp = 600; // sec

    private static final Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
    private static final JWTVerifier verifier = JWT.require(algorithm).build();

    private static final Integer bearerLength = "Bearer ".length();

    private static RedisCommands<String, String> syncCommands;

    public Map<String, String> generateJwtToken(String username, List<String> roles) {
        String accessToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExp))
                .withIssuedAt(new Date())
                .withClaim("roles", roles)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .sign(algorithm);

        syncCommands.set("token:" + username, refreshToken);
        syncCommands.expire("token:" + username, refreshTokenExp);

        log.info("Create token pair access: {}, refresh: {}", accessToken, refreshToken);

        return Map.of("access_token", accessToken,
                "token_type", "bearer",
                "expires_in", String.valueOf(accessTokenExp),
                "refresh_token", refreshToken);
    }

    public void verifiedAccessToken(String authorizationHeader) {
        String token = authorizationHeader.substring(bearerLength);
        DecodedJWT decodedJWT = verifier.verify(token);

        String username = decodedJWT.getSubject();
        List<Role> roles = decodedJWT.getClaim("roles").asList(Role.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, roles);

        log.info("User {} verified.", username);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public Map<String, String> updateAccessToken(String authorizationHeader, UserService userService) {
        String username = getUsernameFromToken(authorizationHeader);

        String tokenFromRequest = authorizationHeader.substring(bearerLength);
        String tokenFromRedis = syncCommands.get("token:" + username);

        if (tokenFromRedis != null && tokenFromRedis.equals(tokenFromRequest)) {

            log.info("found existing refresh token: {}", tokenFromRedis);

            List<String> roles = userService.getUser(username).getRoles()
                    .stream().map(Enum::name).collect(Collectors.toList());

            return generateJwtToken(username, roles);
        } else {
            throw new InvalidTokenException(authorizationHeader);
        }
    }

    public String getUsernameFromToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(bearerLength);
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } else {
            throw new InvalidTokenException(authorizationHeader);
        }
    }

    private static RedisCommands<String, String> setUpRedis() {
        RedisURI localhost = RedisURI.Builder
                .redis(redisHost, redisPort).withPassword(redisPassword.toCharArray())
                .withDatabase(redisDatabase).build();
        RedisClient redisClient = RedisClient.create(localhost);

        try {
            StatefulRedisConnection<String, String> connection = redisClient.connect();
            return connection.sync();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        log.error("Couldn't connect to Redis, host: {}, port: {}, pwd: {}", redisHost, redisPort, redisPassword);
        throw new RuntimeException("Couldn't connect to Redis");
    }
}
