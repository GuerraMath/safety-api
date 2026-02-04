package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        if (token == null || token.trim().isEmpty()) return null;
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.trim().isEmpty()) return false;
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return buildToken(claims, user, jwtExpiration);
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return buildToken(claims, user, refreshExpiration);
    }
    public String generateToken(User user) {
        return generateAccessToken(user);
    }
    public boolean isRefreshToken(String token) {
        if (!isTokenValid(token)) return false;
        String type = extractClaim(token, claims -> claims.get("type", String.class));
        return "refresh".equals(type);
    }

    public String extractUserId(String token) {
        if (token == null || token.trim().isEmpty()) return null;
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            return null;
        }
    }

    private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256) // Sintaxe nova 0.12.x
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser() // MUDANÇA 1: parserBuilder() -> parser()
                .verifyWith(getSignInKey()) // MUDANÇA 2: setSigningKey -> verifyWith
                .build()
                .parseSignedClaims(token) // MUDANÇA 3: parseClaimsJws -> parseSignedClaims
                .getPayload(); // MUDANÇA 4: getBody -> getPayload
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}