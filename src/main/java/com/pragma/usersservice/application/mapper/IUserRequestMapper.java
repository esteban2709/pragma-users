package com.pragma.usersservice.application.mapper;


import com.pragma.usersservice.application.dto.request.UserRequestDto;
import com.pragma.usersservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {

    @Mapping(target = "role.id", source = "role")
    User toUser(UserRequestDto userRequestDto);
}
