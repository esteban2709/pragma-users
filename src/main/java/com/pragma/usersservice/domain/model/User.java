package com.pragma.usersservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private String lastName;
    private Long documentId;
    private String phoneNumber;
    private Date birthDate;
    private String password;
    private String email;
    private Role role;
}
