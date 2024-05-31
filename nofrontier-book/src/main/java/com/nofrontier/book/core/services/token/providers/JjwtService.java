package com.nofrontier.book.core.services.token.providers;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nofrontier.book.core.services.token.adapters.TokenService;
import com.nofrontier.book.domain.exceptions.TokenServiceException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JjwtService implements TokenService {

    @Value("${com.nofrontier.access.key}")
    private String accessKey;

    @Value("${com.nofrontier.access.expiration}")
    private int accessExpiration;

    @Value("${com.nofrontier.refresh.key}")
    private String refreshKey;

    @Value("${com.nofrontier.refresh.expiration}")
    private int refreshExpiration;

    @Override
    public String generateAccessToken(String subject) {
        return generateToken(accessKey, accessExpiration, subject);
    }

    @Override
    public String getSubjectDoAccessToken(String accessToken) {
        var claims = getClaims(accessToken, accessKey);
        return claims.getSubject();
    }

    @Override
    public String generateRefreshToken(String subject) {
        return generateToken(refreshKey, refreshExpiration, subject);
    }

    @Override
    public String getSubjectDoRefreshToken(String refreshToken) {
        var claims = getClaims(refreshToken, refreshKey);
        return claims.getSubject();
    }

    private String generateToken(String signKey, int expiration, String subject) {
        //var claims = new HashMap<String, Object>();
    	Map<String, Object> claims = new HashMap<>();

        var dateCurrentTime = Instant.now();
        var dateExpirationTime = dateCurrentTime.plusSeconds(expiration);

        // Decode the Base64 encoded key
        byte[] decodedKey = Base64.getDecoder().decode(signKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(decodedKey);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date.from(dateCurrentTime))
            .setExpiration(Date.from(dateExpirationTime))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    private Claims getClaims(String token, String signKey) {
        try {
            return tryGetClaims(token, signKey);
        } catch (JwtException exception) {
            throw new TokenServiceException(exception.getLocalizedMessage());
        }
    }

    private Claims tryGetClaims(String token, String signKey) {
        // Decode the Base64 encoded key
        byte[] decodedKey = Base64.getDecoder().decode(signKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(decodedKey);

        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

}

