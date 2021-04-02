package com.bitforce.tuteme.configuration.jwt;

import com.bitforce.tuteme.configuration.TutemeUserDetails;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.StudentRepository;
import com.bitforce.tuteme.repository.TutorRepository;
import com.bitforce.tuteme.repository.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajeevkumarsingh on 19/08/17.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(com.bitforce.tuteme.configuration.jwt.JwtTokenProvider.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TutorRepository tutorRepository;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public Date expiryDate;

    public String generateToken(Authentication authentication) {

        TutemeUserDetails tutemeUserDetails = (TutemeUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(tutemeUserDetails.getId()).get();

        Date now = new Date();
        expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",tutemeUserDetails.getId());
        claims.put("username",tutemeUserDetails.getUsername());
        claims.put("role",tutemeUserDetails.getAuthorities());
        if(user.getType().equals("student")){
            claims.put("studentId",studentRepository.findByUserId(user.getId()).getId());
        }else if (user.getType().equals("tutor")){
            claims.put("tutorId",tutorRepository.findByUserId(user.getId()).getId());
        }
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
