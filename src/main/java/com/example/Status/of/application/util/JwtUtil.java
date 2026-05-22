package com.example.Status.of.application.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//import static jdk.jfr.internal.EventWriterKey.getKey;


//@Component
//public class JwtUtil {
//
//    private static final String SECRET =
//            "mysecretkeymysecretkeymysecretkeymysecretkeymysecretkey1234567890"; // 64+ chars
//
//    private static final long EXPIRATION = 1000 * 60 * 60 * 24;
//
//    private Key getKey() {
//        return Keys.hmacShaKeyFor(SECRET.getBytes());
//    }
//
//    public String generateToken(String mobile) {
//        return Jwts.builder()
//                .setSubject(mobile)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
//                .signWith(getKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String extractUsername(String token) {
//        return extractMobile(token);
//    }
//
//    public String extractMobile(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        try {
//            String mobile = extractMobile(token);
//            return mobile.equals(userDetails.getUsername());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String mobile) {
        System.out.println("SECRET USED FOR GENERATE: " + secret);
        String token = Jwts.builder()
                .setSubject(mobile)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("GENERATED TOKEN: " + token);
        return token;
    }

    public String extractUsername(String token) {
        System.out.println("SECRET USED FOR VALIDATE: " + secret);
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // SAME KEY
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

//    public String extractMobile(String token) {
//        return Jwts.parserBuilder()
////                .setSigningKey(getKey())
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateToken(String token, String userDetails) {
//        try {
//            String mobile = extractMobile(token);
//            return mobile.equals(userDetails);
//        } catch (Exception e) {
//            return false;
//        }

    public boolean validateToken(String token, String mobile) {
        String extracted = extractUsername(token);

        return extracted.equals(mobile);
    }
}
