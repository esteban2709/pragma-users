package com.pragma.usersservice.infraestructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.usersservice.application.dto.request.UserRequestDto;
import com.pragma.usersservice.application.dto.response.UserResponseDto;
import com.pragma.usersservice.application.handler.IUserHandler;
import com.pragma.usersservice.domain.model.Role;
import com.pragma.usersservice.domain.model.User;
import com.pragma.usersservice.infraestructure.input.rest.UserRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @InjectMocks
    private UserRestController userRestController;

    @Mock
    private IUserHandler userHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private Role role;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();
        objectMapper = new ObjectMapper();

        // Setup roles
        role = new Role(1L, "ADMIN", "ADMIN");

        // Setup user request DTO
        userRequestDto = new UserRequestDto();
        userRequestDto.setName("Esteban");
        userRequestDto.setLastName("Gutierrez");
        userRequestDto.setDocumentId(123456789L);
        userRequestDto.setPhoneNumber("1234567890");
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JANUARY, 1);
        userRequestDto.setBirthDate(cal.getTime());
        userRequestDto.setPassword("password");
        userRequestDto.setEmail("esteban@mail.com");
        userRequestDto.setRole(role.getId());

        // Setup user response DTO
        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("Esteban");
        userResponseDto.setLastName("Gutierrez");
        userResponseDto.setDocumentId(123456789L);
        userResponseDto.setPhoneNumber("1234567890");
        userResponseDto.setBirthDate(cal.getTime());
        userResponseDto.setEmail("esteban@mail.com");
        userResponseDto.setRole(role);
    }

    @Test
    @DisplayName("Should save user successfully")
    void saveUserTest() throws Exception {
        // Arrange
        when(userHandler.saveUser(any(UserRequestDto.class))).thenReturn(userResponseDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(userResponseDto.getName()))
                .andExpect(jsonPath("$.lastName").value(userResponseDto.getLastName()))
                .andExpect(jsonPath("$.email").value(userResponseDto.getEmail()));

        verify(userHandler).saveUser(any(UserRequestDto.class));
    }

    @Test
    @DisplayName("Should get all users")
    void getAllUsersTest() throws Exception {
        // Arrange
        List<UserResponseDto> userList = Arrays.asList(userResponseDto);
        when(userHandler.findAllUsers()).thenReturn(userList);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(userResponseDto.getName()))
                .andExpect(jsonPath("$[0].lastName").value(userResponseDto.getLastName()));

        verify(userHandler).findAllUsers();
    }

    @Test
    @DisplayName("Should get user by id")
    void getUserByIdTest() throws Exception {
        // Arrange
        Long userId = 1L;
        when(userHandler.findUserById(userId)).thenReturn(userResponseDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(userResponseDto.getName()))
                .andExpect(jsonPath("$.lastName").value(userResponseDto.getLastName()));

        verify(userHandler).findUserById(userId);
    }

//    @Test
//    @DisplayName("Should update user")
//    void updateUserTest() throws Exception {
//        // Arrange
//        Long userId = 1L;
//        when(userHandler.(any(UserRequestDto.class), any(Long.class))).thenReturn(userResponseDto);
//
//        // Act & Assert
//        mockMvc.perform(put("/api/v1/users/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userRequestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(userResponseDto.getId()))
//                .andExpect(jsonPath("$.name").value(userResponseDto.getName()))
//                .andExpect(jsonPath("$.lastName").value(userResponseDto.getLastName()));
//
//        verify(userHandler).updateUser(any(UserRequestDto.class), any(Long.class));
//    }

//    @Test
//    @DisplayName("Should delete user")
//    void deleteUserTest() throws Exception {
//        // Arrange
//        Long userId = 1L;
//
//        // Act & Assert
//        mockMvc.perform(delete("/api/v1/users/{id}", userId))
//                .andExpect(status().isNoContent());
//
//        verify(userHandler).deleteUser(userId);
//    }
}