package com.hamming.storim.common.dto.protocol;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;

public class LoginResultDTO implements ProtocolDTO {

    private boolean loginSucceeded = false;
    private String errorMessage;
    private UserDto user;
    private LocationDto location;

    public LoginResultDTO(boolean success, String errorMessage, UserDto user, LocationDto location) {
        this.loginSucceeded = success;
        this.errorMessage = errorMessage;
        this.user = user;
        this.location = location;
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

    @Override
    public String toString() {
        return "LoginResultDTO{" +
                "loginSucceeded=" + loginSucceeded +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                ", location=" + location +
                '}';
    }
}
