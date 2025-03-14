package com.pragma.usersservice.infraestructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenUtils {

    private final static long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    private final static String ACCESS_TOKEN_SECRET = "access12token12secretAC12DSaa2s2dasd978";
//    private final static String REFRESH_TOKEN_SECRET = "refresh12token12secretAC12DSaa2s2dasd978";

    public static long getExpirationTime() {
        return EXPIRATION_TIME;
    }

    public static String createAccessToken(String name, String email, Collection<? extends GrantedAuthority> authorities, Long id) {
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(email)
                .claim("name", name)
                .claim("roles", roles)
                .claim("email", email)
                .claim("id", id)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();

    }

//    public static String createRefreshToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes()))
//                .compact();
//    }

    public static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String email = claims.getSubject();

            // Extract roles from token claims
            List<String> roles = claims.get("roles", List.class);
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        } catch (JwtException e){
            return null;
        }
    }

    public static String getTokenFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String token = attributes.getRequest().getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                return token.substring(7); // Remove "Bearer " prefix
            }
        }
        return null;
    }

    public static Long getUserIdFromToken() {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(getTokenFromRequest())
                    .getBody();
            return claims.get("id", Long.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public static String getRoleIdFromToken() {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(getTokenFromRequest())
                    .getBody();
            return claims.get("roles", List.class).get(0).toString();
        } catch (JwtException e) {
            return null;
        }
    }
}
