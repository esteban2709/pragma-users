package com.pragma.usersservice.domain.spi;


import com.pragma.usersservice.domain.model.User;

import java.util.List;

public interface IUserPersistencePort {

    User saveUser(User user);

    User findUserById(Long id);

    List<User> findAllUsers();
}
