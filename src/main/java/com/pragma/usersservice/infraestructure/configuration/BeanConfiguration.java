package com.pragma.usersservice.infraestructure.configuration;

import com.pragma.usersservice.domain.api.IUserServicePort;
import com.pragma.usersservice.domain.spi.IPasswordEncoderPort;
import com.pragma.usersservice.domain.spi.ITokenUtilsPort;
import com.pragma.usersservice.domain.spi.IUserPersistencePort;
import com.pragma.usersservice.domain.usecase.UserUseCase;
import com.pragma.usersservice.infraestructure.helpers.BcryptPasswordEncoder;
import com.pragma.usersservice.infraestructure.out.jpa.adapter.UserJpaAdapter;
import com.pragma.usersservice.infraestructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.usersservice.infraestructure.out.jpa.repository.IUserRepository;
import com.pragma.usersservice.infraestructure.out.utils.TokenUtilsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserUseCase(userPersistencePort(), passwordEncoderPort(), tokenUtilsPort());
    }

    @Bean
    public IPasswordEncoderPort passwordEncoderPort() {
        return new BcryptPasswordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public ITokenUtilsPort tokenUtilsPort() {
        return new TokenUtilsAdapter();
    }
}
