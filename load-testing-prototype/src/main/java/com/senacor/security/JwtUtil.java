package com.senacor.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String jwtSecret = "7c0c24e6dd79e0d18fc281d592d894a7";
    private final String jwtIssuer = "senacor.com";

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {}
//        } catch (SignatureException ex) {
//            logger.error("Invalid JWT signature - {}", ex.getMessage());
//        } catch (MalformedJwtException ex) {
//            logger.error("Invalid JWT token - {}", ex.getMessage());
//        } catch (ExpiredJwtException ex) {
//            logger.error("Expired JWT token - {}", ex.getMessage());
//        } catch (UnsupportedJwtException ex) {
//            logger.error("Unsupported JWT token - {}", ex.getMessage());
//        } catch (IllegalArgumentException ex) {
//            logger.error("JWT claims string is empty - {}", ex.getMessage());
//        }
        return false;
    }
}
