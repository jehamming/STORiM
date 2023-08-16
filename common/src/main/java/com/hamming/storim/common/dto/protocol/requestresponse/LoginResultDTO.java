package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class LoginResultDTO extends ResponseDTO {
    private UserDto user;
    private String token;
    private LocationDto location;

    public LoginResultDTO(boolean success, String token, String errorMessage, UserDto user, LocationDto location) {
        super(success, errorMessage);
        this.user = user;
        this.location = location;
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public LocationDto getLocation() {
        return location;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "LoginResultDTO{" +
                "loginSucceeded=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", user=" + user +
                ", token='" + token + '\'' +
                ", location=" + location +
                '}';
    }
}
