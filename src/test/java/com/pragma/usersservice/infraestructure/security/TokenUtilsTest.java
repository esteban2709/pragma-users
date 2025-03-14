package com.pragma.usersservice.infraestructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenUtilsTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TokenUtils tokenUtils;

    private static final String SECRET_KEY = "access12token12secretAC12DSaa2s2dasd978";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TEST_TOKEN = "testToken";
    private static final String EMAIL = "test@example.com";
    private static final String NAME = "Test User";
    private static final Long ID = 1L;
    private static final List<GrantedAuthority> AUTHORITIES = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void getExpirationTime() {
        long expectedExpirationTime = 24 * 60 * 60 * 1000;
        assertEquals(expectedExpirationTime, TokenUtils.getExpirationTime());
    }

    @Test
    void createAccessToken() {
        String token = tokenUtils.createAccessToken(NAME, EMAIL, AUTHORITIES, ID);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(EMAIL, claims.getSubject());
        assertEquals(NAME, claims.get("name"));
        assertEquals(ID, claims.get("id", Long.class));
        assertEquals(Collections.singletonList("ROLE_USER"), claims.get("roles"));
    }

    @Test
    void getUsernamePasswordAuthenticationToken() {
        String token = tokenUtils.createAccessToken(NAME, EMAIL, AUTHORITIES, ID);
        UsernamePasswordAuthenticationToken authenticationToken = tokenUtils.getUsernamePasswordAuthenticationToken(token);

        assertNotNull(authenticationToken);
        assertEquals(EMAIL, authenticationToken.getPrincipal());
        assertEquals(AUTHORITIES, authenticationToken.getAuthorities());
    }

    @Test
    void getTokenFromRequest() {
        when(request.getHeader("Authorization")).thenReturn(TOKEN_PREFIX + TEST_TOKEN);
        String token = tokenUtils.getTokenFromRequest();
        assertEquals(TEST_TOKEN, token);
    }

    @Test
    void getUserIdFromToken() {
        String token = tokenUtils.createAccessToken(NAME, EMAIL, AUTHORITIES, ID);
        when(request.getHeader("Authorization")).thenReturn(TOKEN_PREFIX + token);
        Long userId = tokenUtils.getUserIdFromToken();
        assertEquals(ID, userId);
    }

    @Test
    void getRoleIdFromToken() {
        String token = tokenUtils.createAccessToken(NAME, EMAIL, AUTHORITIES, ID);
        when(request.getHeader("Authorization")).thenReturn(TOKEN_PREFIX + token);
        String roleId = tokenUtils.getRoleIdFromToken();
        assertEquals("ROLE_USER", roleId);
    }
}