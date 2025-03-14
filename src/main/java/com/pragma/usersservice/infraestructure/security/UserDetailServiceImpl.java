package com.pragma.usersservice.infraestructure.security;

import com.pragma.usersservice.infraestructure.exeptionhandler.ExceptionResponse;
import com.pragma.usersservice.infraestructure.out.jpa.entity.UserEntity;
import com.pragma.usersservice.infraestructure.out.jpa.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity =  userRepository.findOneByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionResponse.USER_NOT_FOUND.getMessage()));
        return new UserDetailsImpl(userEntity);
    }
}
