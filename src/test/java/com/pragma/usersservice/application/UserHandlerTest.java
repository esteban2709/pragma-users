package com.pragma.usersservice.application.handler.impl;

import com.pragma.usersservice.application.dto.request.UserRequestDto;
import com.pragma.usersservice.application.dto.response.UserResponseDto;
import com.pragma.usersservice.application.mapper.IUserRequestMapper;
import com.pragma.usersservice.application.mapper.IUserResponseMapper;
import com.pragma.usersservice.domain.api.IUserServicePort;
import com.pragma.usersservice.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @InjectMocks
    private UserHandler userHandler;

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private IUserResponseMapper userResponseMapper;

    @Mock
    private IUserRequestMapper userRequestMapper;

    private User user;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        // Mock data setup
        user = new User();
        user.setId(1L);
        user.setName("Esteban");
        user.setLastName("Gutierrez");
        user.setDocumentId(123456789L);
        user.setPhoneNumber("1234567890");
        user.setBirthDate(new Date());
        user.setPassword("password");
        user.setEmail("esteban@mail.com");

        userRequestDto = new UserRequestDto();
        userRequestDto.setName("Esteban");
        userRequestDto.setLastName("Gutierrez");
        userRequestDto.setDocumentId(123456789L);
        userRequestDto.setPhoneNumber("1234567890");
        userRequestDto.setBirthDate(new Date());
        userRequestDto.setPassword("password");
        userRequestDto.setEmail("esteban@mail.com");

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("Esteban");
        userResponseDto.setLastName("Gutierrez");
        userResponseDto.setDocumentId(123456789L);
        userResponseDto.setPhoneNumber("1234567890");
        userResponseDto.setEmail("esteban@mail.com");
    }

    @Test
    @DisplayName("Should save user successfully")
    void saveUserTest() {
        // Arrange
        when(userRequestMapper.toUser(any(UserRequestDto.class))).thenReturn(user);
        when(userServicePort.saveUser(any(User.class))).thenReturn(user);
        when(userResponseMapper.toResponse(any(User.class))).thenReturn(userResponseDto);

        // Act
        UserResponseDto result = userHandler.saveUser(userRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(userResponseDto.getId(), result.getId());
        assertEquals(userResponseDto.getName(), result.getName());
        assertEquals(userResponseDto.getLastName(), result.getLastName());
        verify(userRequestMapper, times(1)).toUser(userRequestDto);
        verify(userServicePort, times(1)).saveUser(user);
        verify(userResponseMapper, times(1)).toResponse(user);
    }

    @Test
    @DisplayName("Should find all users")
    void findAllUsersTest() {
        // Arrange
        List<User> users = Arrays.asList(user);
        List<UserResponseDto> userResponseDtos = Arrays.asList(userResponseDto);

        when(userServicePort.findAllUsers()).thenReturn(users);
        when(userResponseMapper.toResponseList(anyList())).thenReturn(userResponseDtos);

        // Act
        List<UserResponseDto> results = userHandler.findAllUsers();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(userResponseDto.getId(), results.get(0).getId());
        verify(userServicePort, times(1)).findAllUsers();
        verify(userResponseMapper, times(1)).toResponseList(users);
    }

    @Test
    @DisplayName("Should find user by id")
    void findUserByIdTest() {
        // Arrange
        Long userId = 1L;
        when(userServicePort.findUserById(userId)).thenReturn(user);
        when(userResponseMapper.toResponse(any(User.class))).thenReturn(userResponseDto);

        // Act
        UserResponseDto result = userHandler.findUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userResponseDto.getId(), result.getId());
        assertEquals(userResponseDto.getName(), result.getName());
        verify(userServicePort, times(1)).findUserById(userId);
        verify(userResponseMapper, times(1)).toResponse(user);
    }
}