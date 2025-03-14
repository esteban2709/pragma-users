package com.pragma.usersservice.infraestructure.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthCredentials {
    private String email;
    private String password;
}
