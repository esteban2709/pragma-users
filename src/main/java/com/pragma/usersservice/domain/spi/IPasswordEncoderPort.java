package com.pragma.usersservice.domain.spi;

public interface IPasswordEncoderPort {

    String encode(String password);
}
