package com.pragma.usersservice.infraestructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class JWTAuthorizationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal() throws ServletException, IOException {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIn0.1234567890";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                "test@example.com", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        try (MockedStatic<TokenUtils> tokenUtilsMock = mockStatic(TokenUtils.class)) {
            tokenUtilsMock.when(() -> TokenUtils.getUsernamePasswordAuthenticationToken(token))
                    .thenReturn(authToken);

            try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
                securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);

                // Act
                jwtAuthorizationFilter.doFilterInternal(request, response, chain);

                // Assert
                verify(securityContext).setAuthentication(authToken);
                verify(chain).doFilter(request, response);
                tokenUtilsMock.verify(() -> TokenUtils.getUsernamePasswordAuthenticationToken(token));
            }
        }
    }
}