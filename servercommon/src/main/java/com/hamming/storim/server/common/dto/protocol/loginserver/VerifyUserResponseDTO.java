package com.hamming.storim.server.common.dto.protocol.loginserver;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class VerifyUserResponseDTO extends ResponseDTO {

    private UserDto user;
    private String errorMessage;

    public VerifyUserResponseDTO(UserDto user, String errorMessage) {
        this.user = user;
        this.errorMessage = errorMessage;
    }

    public UserDto getUser() {
        return user;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "VerifyUserResponseDTO{" +
                "user=" + user +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
