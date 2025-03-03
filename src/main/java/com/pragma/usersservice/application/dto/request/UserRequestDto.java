package com.pragma.usersservice.application.dto.request;

import com.pragma.plazoleta.domain.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserRequestDto {
    private String name;
    private String lastName;
    private String documentId;
    private String phoneNumber;
    private Date birthDate;
    private String password;
    private String email;
    private Role role;
}
