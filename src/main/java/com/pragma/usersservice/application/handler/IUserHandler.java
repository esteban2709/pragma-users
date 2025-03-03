package com.pragma.usersservice.application.handler;


import com.pragma.usersservice.application.dto.request.UserRequestDto;
import com.pragma.usersservice.application.dto.response.UserResponseDto;

import java.util.List;

public interface IUserHandler {

    UserResponseDto saveUser(UserRequestDto userRequestDto);

    List<UserResponseDto> findAllUsers();

    UserResponseDto findUserById(Long id);
}
