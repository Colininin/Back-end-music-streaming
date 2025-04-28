package nl.colin.s3.beeple.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.expiration}")
    private long accessTokenValidity;

    @Value("${jwt.refreshExpiration}")
    private long refreshTokenValidity;

    public long getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public long getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public String generateToken(String username, String role) {
        return createTokenWithValidity(username, accessTokenValidity, role);
    }

    public String generateRefreshToken(String username, String role) {
        return createTokenWithValidity(username, refreshTokenValidity, role);
    }

    private String createTokenWithValidity(String subject, long validity, String role) {
        Map<String, Object> claims = createClaimsForUser(role);
        return buildToken(claims, subject, validity);
    }

    private Map<String, Object> createClaimsForUser(String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return claims;
    }

    private String buildToken(Map<String, Object> claims, String subject, long validity) {
        long nowMillis = System.currentTimeMillis();
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(nowMillis + validity))
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .compact();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
