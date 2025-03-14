package com.pragma.usersservice.infraestructure.out.utils;

import com.pragma.usersservice.domain.spi.ITokenUtilsPort;
import com.pragma.usersservice.infraestructure.security.TokenUtils;

public class TokenUtilsAdapter implements ITokenUtilsPort {

    @Override
    public String getRole() {
        return TokenUtils.getRoleIdFromToken();
    }

    @Override
    public Long getUserId() {
        return TokenUtils.getUserIdFromToken();
    }
}
