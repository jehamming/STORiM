package com.hamming.storim.server.common.dto.protocol.dataserver;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class VerifyUserTokenResponseDTO extends ResponseDTO {


    private boolean success = false;
    private String errorMessage;
    private UserDto user;

    public VerifyUserTokenResponseDTO(boolean success, String errorMessage, UserDto user) {
        this.user = user;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public UserDto getUser() {
        return user;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "VerifyUserTokenResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                '}';
    }
}
