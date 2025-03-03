package com.pragma.usersservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;
    private String description;
}
// script para crear role propietario
// insert into role (id, name, description) values (1, 'Propietario', 'propietario de restaurante');