package com.pragma.usersservice.application.handler;

import com.pragma.plazoleta.application.dto.response.UserResponseDto;

import java.util.List;

public interface IUserHandler {

    UserResponseDto saveUser(UserResponseDto userResponseDto);

    List<UserResponseDto> findAllUsers();
}
