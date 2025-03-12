package com.pragma.usersservice.domain;

import com.pragma.usersservice.domain.exception.CustomException;
import com.pragma.usersservice.domain.exception.UserIsNotLegalAge;
import com.pragma.usersservice.domain.model.Role;
import com.pragma.usersservice.domain.model.User;
import com.pragma.usersservice.domain.spi.IPasswordEncoderPort;
import com.pragma.usersservice.domain.spi.ITokenUtilsPort;
import com.pragma.usersservice.domain.spi.IUserPersistencePort;
import com.pragma.usersservice.domain.usecase.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @InjectMocks
    private UserUseCase userUseCase;

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private ITokenUtilsPort tokenUtilsPort;

    @Mock
    private IPasswordEncoderPort passwordEncoder;

    private User user;
    private Role adminRole;
    private Role ownerRole;
    private Role employeeRole;
    private Role clientRole;

    @BeforeEach
    void setUp() {
        // Setup roles
        adminRole = new Role(1L, "ADMIN", "ADMIN");
        ownerRole = new Role(2L, "OWNER", "OWNER");
        employeeRole = new Role(3L, "EMPLOYEE", "EMPLOYEE");
        clientRole = new Role(4L, "CUSTOMER", "CUSTOMER");

        // Setup user
        user = new User();
        user.setId(1L);
        user.setName("Esteban");
        user.setLastName("Gutierrez");
        user.setDocumentId(123456789L);
        user.setPhoneNumber("1234567890");
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JANUARY, 1);
        user.setBirthDate(cal.getTime());
        user.setPassword("password");
        user.setEmail("esteban@mail.com");
        user.setRole(adminRole);

        // Setup mocks
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    @DisplayName("Should save user successfully")
    void saveUserTest() {
        // Arrange
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userUseCase.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        verify(passwordEncoder).encode("password");
        verify(userPersistencePort).saveUser(user);
    }

    @Test
    @DisplayName("Should throw exception when user is under 18")
    void saveUserUnderAgeTest() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -17); // 17 years ago
        user.setBirthDate(cal.getTime());

        // Act & Assert
        assertThrows(UserIsNotLegalAge.class, () -> userUseCase.saveUser(user));
        verify(userPersistencePort, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Should allow any user to create a CUSTOMER account")
    void saveCustomerUserTest() {
        // Arrange
        user.setRole(clientRole);
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userUseCase.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(clientRole.getId(), savedUser.getRole().getId());
        verify(userPersistencePort).saveUser(user);
    }

    @Test
    @DisplayName("Should allow ADMIN to create OWNER accounts")
    void adminCreatesOwnerTest() {
        // Arrange
        user.setRole(ownerRole);
        when(tokenUtilsPort.getRole()).thenReturn("ADMIN");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userUseCase.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(ownerRole.getId(), savedUser.getRole().getId());
        verify(tokenUtilsPort).getRole();
        verify(userPersistencePort).saveUser(user);
    }

    @Test
    @DisplayName("Should throw exception when non-ADMIN tries to create OWNER")
    void nonAdminCreatesOwnerTest() {
        // Arrange
        user.setRole(ownerRole);
        when(tokenUtilsPort.getRole()).thenReturn("OWNER");

        // Act & Assert
        assertThrows(CustomException.class, () -> userUseCase.saveUser(user));
        verify(tokenUtilsPort).getRole();
        verify(userPersistencePort, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Should allow OWNER to create EMPLOYEE accounts")
    void ownerCreatesEmployeeTest() {
        // Arrange
        user.setRole(employeeRole);
        when(tokenUtilsPort.getRole()).thenReturn("OWNER");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userUseCase.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(employeeRole.getId(), savedUser.getRole().getId());
        verify(tokenUtilsPort).getRole();
        verify(userPersistencePort).saveUser(user);
    }

    @Test
    @DisplayName("Should throw exception when non-OWNER tries to create EMPLOYEE")
    void nonOwnerCreatesEmployeeTest() {
        // Arrange
        user.setRole(employeeRole);
        when(tokenUtilsPort.getRole()).thenReturn("ADMIN");

        // Act & Assert
        assertThrows(CustomException.class, () -> userUseCase.saveUser(user));
        verify(tokenUtilsPort).getRole();
        verify(userPersistencePort, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Should find all users")
    void findAllUsersTest() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(userPersistencePort.findAllUsers()).thenReturn(users);

        // Act
        List<User> foundUsers = userUseCase.findAllUsers();

        // Assert
        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals(user.getId(), foundUsers.get(0).getId());
        verify(userPersistencePort).findAllUsers();
    }

    @Test
    @DisplayName("Should find user by id")
    void findUserByIdTest() {
        // Arrange
        Long userId = 1L;
        when(userPersistencePort.findUserById(userId)).thenReturn(user);

        // Act
        User foundUser = userUseCase.findUserById(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        verify(userPersistencePort).findUserById(userId);
    }
}