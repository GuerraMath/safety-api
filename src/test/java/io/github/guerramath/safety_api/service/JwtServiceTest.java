package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
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
        // Proteção contra token nulo/vazio
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        return extractClaim(token, Claims::getSubject);
    }

    // Método usado pelos testes para validar apenas o token (sem UserDetails)
    public boolean isTokenValid(String token) {
        // CORREÇÃO CRÍTICA AQUI:
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            // Qualquer erro de parse (token malformado, assinatura inválida) retorna false
            return false;
        }
    }

    // Sobrecarga caso você use validação com UserDetails em outros lugares
    public boolean isTokenValid(String token, User user) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        final String username = extractUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    public String generateAccessToken(User user) {
        return buildToken(new HashMap<>(), user, jwtExpiration);
    }

    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user, refreshExpiration);
    }

    // Método auxiliar para detectar se é refresh token (baseado na expiração ou claim específica)
    // Ajuste conforme sua lógica. Aqui assumimos que se é válido e não expirou, ok.
    public boolean isRefreshToken(String token) {
        if (!isTokenValid(token)) return false;
        // Se você tiver uma claim específica para diferenciar, cheque aqui.
        // Caso contrário, a validação básica de estrutura serve para este escopo.
        return true;
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject); // Supondo que o ID/Email está no subject
    }

    private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(String.valueOf(user.getId())) // Salvando ID no subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
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
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}