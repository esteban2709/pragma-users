package com.pragma.usersservice.application.handler.impl;

import com.pragma.usersservice.application.dto.request.UserRequestDto;
import com.pragma.usersservice.application.dto.response.UserResponseDto;
import com.pragma.usersservice.application.handler.IUserHandler;
import com.pragma.usersservice.application.mapper.IUserRequestMapper;
import com.pragma.usersservice.application.mapper.IUserResponseMapper;
import com.pragma.usersservice.domain.api.IUserServicePort;
import com.pragma.usersservice.domain.model.User;
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
    public UserResponseDto saveUser(UserRequestDto userRequestDto) {
        User user = userRequestMapper.toUser(userRequestDto);
        return userResponseMapper.toResponse(userServicePort.saveUser(user));
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        return userResponseMapper.toResponseList(userServicePort.findAllUsers());
    }

    @Override
    public UserResponseDto findUserById(Long id) {
        return userResponseMapper.toResponse(userServicePort.findUserById(id));
    }
}
