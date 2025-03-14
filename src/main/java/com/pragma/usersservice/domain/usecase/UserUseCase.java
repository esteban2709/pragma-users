package com.pragma.usersservice.domain.usecase;

import com.pragma.usersservice.domain.api.IUserServicePort;
import com.pragma.usersservice.domain.exception.CustomException;
import com.pragma.usersservice.domain.exception.ExceptionMessage;
import com.pragma.usersservice.domain.exception.UserIsNotLegalAge;
import com.pragma.usersservice.domain.model.User;
import com.pragma.usersservice.domain.spi.IPasswordEncoderPort;
import com.pragma.usersservice.domain.spi.ITokenUtilsPort;
import com.pragma.usersservice.domain.spi.IUserPersistencePort;
import com.pragma.usersservice.domain.utils.Constants;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoder;
    private final ITokenUtilsPort tokenUtilsPort;

    public UserUseCase(IUserPersistencePort userPersistencePort, IPasswordEncoderPort passwordEncoder, ITokenUtilsPort tokenUtilsPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoder = passwordEncoder;
        this.tokenUtilsPort = tokenUtilsPort;
    }

    @Override
    public User saveUser(User user) {
        validateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userPersistencePort.saveUser(user);
    }

    private void validateUser(User user) {
        if (!isUserOlderThan18(user.getBirthDate())) {
            throw new UserIsNotLegalAge();
        }
        if (user.getRole().getId() != 4) {
            validateRoleAssignment(user);
        }
    }

    private void validateRoleAssignment(User user) {
        String role = tokenUtilsPort.getRole();
        if ("2".equals(user.getRole().getId().toString())) {
            if (!Constants.ADMIN.equals(role)) {
                throw new CustomException(ExceptionMessage.ONLY_ADMIN_CAN_CREATE_OWNER.getMessage());
            }
        } else if ("3".equals(user.getRole().getId().toString())) {
            if (!Constants.OWNER.equals(role)) {
                throw new CustomException(ExceptionMessage.ONLY_OWNER_CAN_CREATE_EMPLOYEE.getMessage());
            }
        }
    }

    private boolean isUserOlderThan18(Date birthDate) {
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthLocalDate, LocalDate.now()).getYears() >= Constants.MINIMUM_AGE;
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
