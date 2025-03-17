package com.example.medical.store.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Key REFRESH_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final long EXPIRATION_TIME = 1000*60*1; //1   minutes
    private static final long REFRESH_EXPIRATION_TIME = 1000*60*60*24; //24 hours

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(long userId,String email, String role){
        return Jwts.builder()
                .setSubject(email)
                .claim("id", userId)
                .claim("role","ROLE_" + role.toUpperCase())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractUsernameFromRefreshToken(String token){
        return extractClaimFromRefreshToken(token, Claims::getSubject);
    }

    private <T> T extractClaimFromRefreshToken(String token,Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaimsFromRefreshToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaimsFromRefreshToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(REFRESH_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateRefreshToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(REFRESH_SECRET_KEY,SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateRefreshToken(String token, String username){
        try{
            final String extractedUsername = extractUsernameFromRefreshToken(token);
            return (extractedUsername.equals(username) && !isRefreshTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isRefreshTokenExpired(String token){
        return extractClaimFromRefreshToken(token, Claims::getExpiration).before(new Date());
    }
}



























