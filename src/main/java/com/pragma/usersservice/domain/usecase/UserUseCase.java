package com.pragma.usersservice.domain.usecase;

import com.pragma.usersservice.domain.api.IUserServicePort;
import com.pragma.usersservice.domain.exception.UserIsNotLegalAge;
import com.pragma.usersservice.domain.model.User;
import com.pragma.usersservice.domain.spi.IPasswordEncoderPort;
import com.pragma.usersservice.domain.spi.IUserPersistencePort;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class UserUseCase  implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoder;

    public UserUseCase(IUserPersistencePort userPersistencePort, IPasswordEncoderPort passwordEncoder) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        if (!isUserOlderThan18(user.getBirthDate())) {
            throw new UserIsNotLegalAge();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userPersistencePort.saveUser(user);
    }

    private boolean isUserOlderThan18(Date birthDate) {
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthLocalDate, LocalDate.now()).getYears() >= 18;
    }

    @Override
    public List<User> findAllUsers() {
        return userPersistencePort.findAllUsers();
    }

    @Override
    public User findUserById(Long id) {
        return userPersistencePort.findUserById(id);
    }
}
