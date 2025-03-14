package com.pragma.usersservice.infraestructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.usersservice.infraestructure.out.jpa.entity.RoleEntity;
import com.pragma.usersservice.infraestructure.out.jpa.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.*;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class JWTAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    }

    @Test
    void attemptAuthentication() throws IOException {
        AuthCredentials authCredentials = new AuthCredentials("user@example.com", "password");
        String json = "{\"email\":\"user@example.com\",\"password\":\"password\"}";
        BufferedReader reader = new BufferedReader(new StringReader(json));

        when(request.getReader()).thenReturn(reader);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                authCredentials.getEmail(),
                authCredentials.getPassword(),
                Collections.emptyList()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));

        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        assertNotNull(result);
        verify(authenticationManager).authenticate(authRequest);
    }

    @Test
    void successfulAuthentication() throws IOException, ServletException {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("ROLE_USER");
        roleEntity.setDescription("User role");
        UserEntity user = new UserEntity(1L, "Andres", "Lopez", 123456L, "3003193562", new Date("2000/10/01"), "123456", "a@a.com", 1L, roleEntity);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authResult = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationFilter.successfulAuthentication(request, response, chain, authResult);

        verify(response).addHeader(eq("Authorization"), anyString());
        printWriter.flush(); // Asegúrate de que el PrintWriter se haya vaciado
    }
}