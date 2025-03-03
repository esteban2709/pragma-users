package com.pragma.usersservice.domain.spi;

public interface IPasswordEncoder {

    String encode(String password);
}
