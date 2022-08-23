package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ValidateUserResponseDTO extends ResponseDTO {

    private UserDto user;
    private String errorMessage;

    public ValidateUserResponseDTO(UserDto user, String errorMessage) {
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
        return "ValidateUserResponseDTO{" +
                "user=" + user +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
