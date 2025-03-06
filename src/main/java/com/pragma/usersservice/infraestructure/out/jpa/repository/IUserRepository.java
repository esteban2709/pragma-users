package com.pragma.usersservice.infraestructure.out.jpa.repository;

import com.pragma.usersservice.infraestructure.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findOneByEmail(String email);
}
