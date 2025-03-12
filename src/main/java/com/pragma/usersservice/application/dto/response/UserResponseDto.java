package com.pragma.usersservice.application.dto.response;

import com.pragma.usersservice.domain.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String name;
    private String lastName;
    private Long documentId;
    private String phoneNumber;
    private Date birthDate;
    private String password;
    private String email;
    private Long restaurantId;
    private Role role;

}
