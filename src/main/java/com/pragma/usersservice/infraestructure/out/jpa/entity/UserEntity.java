package com.pragma.usersservice.infraestructure.out.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class UserEntity {


    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String state;
    private String role;
}
