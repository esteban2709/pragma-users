package com.pragma.usersservice.infraestructure.out.jpa.adapter;

import com.pragma.usersservice.domain.model.User;
import com.pragma.usersservice.infraestructure.exception.NoDataFoundException;
import com.pragma.usersservice.infraestructure.out.jpa.entity.UserEntity;
import com.pragma.usersservice.infraestructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.usersservice.infraestructure.out.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJpaAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserEntityMapper userEntityMapper;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    private User user;
    private UserEntity userEntity;
    private List<User> userList;
    private List<UserEntity> userEntityList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Test User");

        userList = Collections.singletonList(user);
        userEntityList = Collections.singletonList(userEntity);
    }

    @Test
    void saveUser_ShouldReturnSavedUser() {
        // Arrange
        when(userEntityMapper.toEntity(any(User.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userEntityMapper.toUser(any(UserEntity.class))).thenReturn(user);

        // Act
        User result = userJpaAdapter.saveUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        verify(userEntityMapper).toEntity(user);
        verify(userRepository).save(userEntity);
        verify(userEntityMapper).toUser(userEntity);
    }

    @Test
    void findUserById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toUser(userEntity)).thenReturn(user);

        // Act
        User result = userJpaAdapter.findUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        verify(userRepository).findById(1L);
        verify(userEntityMapper).toUser(userEntity);
    }

    @Test
    void findUserById_ShouldThrowNoDataFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoDataFoundException.class, () -> userJpaAdapter.findUserById(1L));
        verify(userRepository).findById(1L);
        verifyNoInteractions(userEntityMapper);
    }

    @Test
    void findAllUsers_ShouldReturnUserList_WhenUsersExist() {
        // Arrange
        when(userRepository.findAll()).thenReturn(userEntityList);
        when(userEntityMapper.toUserList(userEntityList)).thenReturn(userList);

        // Act
        List<User> result = userJpaAdapter.findAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getId());
        assertEquals(user.getName(), result.get(0).getName());
        verify(userRepository).findAll();
        verify(userEntityMapper).toUserList(userEntityList);
    }

    @Test
    void findAllUsers_ShouldThrowNoDataFoundException_WhenNoUsersExist() {
        // Arrange
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoDataFoundException.class, () -> userJpaAdapter.findAllUsers());
        verify(userRepository).findAll();
        verifyNoInteractions(userEntityMapper);
    }
}