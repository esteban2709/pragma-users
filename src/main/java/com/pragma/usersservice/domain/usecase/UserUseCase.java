package com.pragma.usersservice.domain.usecase;

import com.pragma.usersservice.domain.api.IUserServicePort;
import com.pragma.usersservice.domain.exception.CustomException;
import com.pragma.usersservice.domain.exception.UserIsNotLegalAge;
import com.pragma.usersservice.domain.model.User;
import com.pragma.usersservice.domain.spi.IPasswordEncoderPort;
import com.pragma.usersservice.domain.spi.ITokenUtilsPort;
import com.pragma.usersservice.domain.spi.IUserPersistencePort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserUseCase  implements IUserServicePort {

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
        if (!isUserOlderThan18(user.getBirthDate())) {
            throw new UserIsNotLegalAge();
        }
        validateRoleAssignment(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userPersistencePort.saveUser(user);
    }

    private void validateRoleAssignment(User user) {
        // Get the authenticated user from SecurityContext
        String role = tokenUtilsPort.getRole();
        // Check role-based permissions
        if ("2".equals(user.getRole().getId().toString())) {
            // Only ADMIN can create OWNER users
            boolean isAdmin = "ADMIN".equals(role);
            if (!isAdmin) {
                throw new CustomException("Only administrators can create owner accounts");
            }
        } else if ("3".equals(user.getRole().getId().toString())) {
            // Only OWNER can create EMPLOYEE users
            boolean isOwner = "OWNER".equals(role);
            if (!isOwner) {
                throw new CustomException("Only owners can create employee accounts");
            }
        }
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
