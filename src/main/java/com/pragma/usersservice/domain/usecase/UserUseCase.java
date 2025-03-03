package com.pragma.usersservice.domain.usecase;

import com.pragma.plazoleta.domain.api.IUserServicePort;
import com.pragma.plazoleta.domain.model.User;
import com.pragma.plazoleta.domain.spi.IUserPersistencePort;

import java.util.List;

public class UserUseCase  implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public User saveUser(User user) {
        return userPersistencePort.saveUser(user);
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }
}
