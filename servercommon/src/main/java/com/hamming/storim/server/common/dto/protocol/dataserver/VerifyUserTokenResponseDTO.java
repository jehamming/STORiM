package com.hamming.storim.server.common.dto.protocol.dataserver;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class VerifyUserTokenResponseDTO extends ResponseDTO {

    private UserDto user;

    public VerifyUserTokenResponseDTO(boolean success, String errorMessage, UserDto user) {
        super(success, errorMessage);
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "VerifyUserTokenResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", user=" + user +
                '}';
    }
}
