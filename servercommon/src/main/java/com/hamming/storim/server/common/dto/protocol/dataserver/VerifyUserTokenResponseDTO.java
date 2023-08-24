package com.hamming.storim.server.common.dto.protocol.dataserver;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class VerifyUserTokenResponseDTO extends ResponseDTO {

    private UserDto user;
    private boolean admin;

    public VerifyUserTokenResponseDTO(boolean success, String errorMessage, UserDto user, boolean admin) {
        super(success, errorMessage);
        this.user = user;
        this.admin = admin;
    }

    public UserDto getUser() {
        return user;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return "VerifyUserTokenResponseDTO{" +
                "user=" + user +
                ", admin=" + admin +
                '}';
    }
}
