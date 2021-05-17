package com.bitforce.tuteme.configuration;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

//    @Value("${app.jwtSecret}")
//    private String secret;
//
//    @Value("${app.jwtExpirationInMs}")
//    private int expiresIn;

    @Autowired
    private AppProperties appProperties;

    public String generate(Authentication authentication) {
        TutemeUserDetails principal = (TutemeUserDetails) authentication.getPrincipal();
        return Jwts
                .builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + appProperties.getAuth().getJwtExpirationInMs()))
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getJwtSecret())
                .compact();
    }

    public String getUsername(String token) {
        return Jwts
                .parser()
                .setSigningKey(appProperties.getAuth().getJwtSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String parse(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        return null;
    }


    public boolean validate(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getJwtSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}

