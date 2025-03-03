package com.pragma.usersservice.infraestructure.out.jpa.adapter;

import com.pragma.usersservice.domain.model.User;
import com.pragma.usersservice.domain.spi.IUserPersistencePort;
import com.pragma.usersservice.infraestructure.exception.NoDataFoundException;
import com.pragma.usersservice.infraestructure.out.jpa.entity.UserEntity;
import com.pragma.usersservice.infraestructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.usersservice.infraestructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public User saveUser(User user) {
        UserEntity userEntity = userRepository.save(userEntityMapper.toEntity(user));
        return userEntityMapper.toUser(userEntity);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).map(userEntityMapper::toUser)
                .orElseThrow(NoDataFoundException::new);

    }

    @Override
    public List<User> findAllUsers() {
        List<UserEntity> entityList = userRepository.findAll();
        if (entityList.isEmpty()) {
            throw new NoDataFoundException();
        }
        return userEntityMapper.toUserList(entityList);
    }
}
