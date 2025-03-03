package com.pragma.usersservice.application.handler.impl;

import com.pragma.plazoleta.application.dto.response.UserResponseDto;
import com.pragma.plazoleta.application.handler.IUserHandler;
import com.pragma.plazoleta.application.mapper.IUserRequestMapper;
import com.pragma.plazoleta.application.mapper.IUserResponseMapper;
import com.pragma.plazoleta.domain.api.IUserServicePort;
import com.pragma.plazoleta.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IUserResponseMapper userResponseMapper;
    private final IUserRequestMapper userRequestMapper;


    @Override
    public UserResponseDto saveUser(UserResponseDto userResponseDto) {
        User user = userRequestMapper.toUser(userResponseDto);
        return userResponseMapper.toResponse(userServicePort.saveUser(user));
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        return userResponseMapper.toResponseList(userServicePort.findAllUsers());
    }
}
