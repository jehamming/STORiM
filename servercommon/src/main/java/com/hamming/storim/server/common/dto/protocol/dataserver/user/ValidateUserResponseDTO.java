package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.SessionDto;

public class ValidateUserResponseDTO extends ResponseDTO {

    private UserDto user;

    private String sessionToken;

    public ValidateUserResponseDTO(boolean success, UserDto user, String errorMessage, String token) {
        super(success, errorMessage);
        this.user = user;
        this.sessionToken = token;
    }

    public UserDto getUser() {
        return user;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    @Override
    public String toString() {
        return "ValidateUserResponseDTO{" +
                "user=" + user +
                ", success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                '}';
    }
}
