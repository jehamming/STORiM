package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class LoginResultDTO implements  ResponseDTO {

    private boolean loginSucceeded = false;
    private String errorMessage;
    private UserDto user;
    private String token;
    private LocationDto location;

    public LoginResultDTO(boolean success, String token, String errorMessage, UserDto user, LocationDto location) {
        this.loginSucceeded = success;
        this.errorMessage = errorMessage;
        this.user = user;
        this.location = location;
        this.token = token;
    }

    public boolean isLoginSucceeded() {
        return loginSucceeded;
    }

    public String getErrorMessage() {
        return errorMessage;
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
                "loginSucceeded=" + loginSucceeded +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                ", token='" + token + '\'' +
                ", location=" + location +
                '}';
    }
}
