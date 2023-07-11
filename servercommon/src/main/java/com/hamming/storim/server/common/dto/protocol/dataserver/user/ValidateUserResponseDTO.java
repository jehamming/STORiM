package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.SessionDto;

public class ValidateUserResponseDTO extends ResponseDTO {

    private UserDto user;
    private String errorMessage;

    private String sessionToken;

    public ValidateUserResponseDTO(UserDto user, String errorMessage, String token) {
        this.user = user;
        this.errorMessage = errorMessage;
        this.sessionToken = token;
    }

    public UserDto getUser() {
        return user;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    @Override
    public String toString() {
        return "ValidateUserResponseDTO{" +
                "user=" + user +
                ", errorMessage='" + errorMessage + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                '}';
    }
}
