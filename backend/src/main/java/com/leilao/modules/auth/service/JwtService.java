package com.leilao.modules.auth.service;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.shared.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service para operações com JWT
 */
@Service
public class JwtService {

    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    @Value("${jwt.expiration:900000}") // 15 minutos
    private long jwtExpiration;

    @Value("${jwt.refresh-token.expiration:604800000}") // 7 dias
    private long refreshExpiration;

    /**
     * Extrai o username (email) do token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai uma claim específica do token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Gera token para o usuário
     */
    public String generateToken(Usuario usuario) {
        return generateToken(buildClaims(usuario), usuario.getEmail());
    }

    /**
     * Gera token com claims extras
     */
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return buildToken(extraClaims, username, jwtExpiration);
    }

    /**
     * Gera refresh token
     */
    public String generateRefreshToken(Usuario usuario) {
        return buildToken(new HashMap<>(), usuario.getEmail(), refreshExpiration);
    }

    /**
     * Constrói o token
     */
    private String buildToken(Map<String, Object> extraClaims, String username, long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Verifica se o token é válido para Usuario
     */
    public boolean isTokenValid(String token, Usuario usuario) {
        final String username = extractUsername(token);
        return (username.equals(usuario.getEmail())) && !isTokenExpired(token);
    }

    /**
     * Verifica se o token é válido para UserDetails
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Verifica se o token expirou
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração do token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai todas as claims do token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtém a chave de assinatura
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Constrói as claims do usuário
     */
    private Map<String, Object> buildClaims(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId().toString());
        claims.put("name", usuario.getNome());
        claims.put("status", usuario.getStatus().name());
        claims.put("roles", usuario.getRoles().stream()
                .map(UserRole::name)
                .collect(Collectors.toList()));
        claims.put("emailVerified", usuario.getEmailVerificado());
        return claims;
    }

    /**
     * Extrai roles do token
     */
    @SuppressWarnings("unchecked")
    public Set<UserRole> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return ((java.util.List<String>) claims.get("roles")).stream()
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
    }

    /**
     * Extrai ID do usuário do token
     */
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }
}