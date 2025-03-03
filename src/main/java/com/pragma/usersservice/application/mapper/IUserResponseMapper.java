package com.pragma.usersservice.application.mapper;

import com.pragma.plazoleta.application.dto.response.UserResponseDto;
import com.pragma.plazoleta.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserResponseMapper {

    UserResponseDto toResponse(User user);

    List<UserResponseDto> toResponseList(List<User> userList);
}
