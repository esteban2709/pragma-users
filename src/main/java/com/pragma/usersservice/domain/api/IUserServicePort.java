package com.pragma.usersservice.domain.api;

import com.pragma.plazoleta.domain.model.User;

import java.util.List;

public interface IUserServicePort {

    User saveUser(User user);

    List<User> findAllUsers();
}
