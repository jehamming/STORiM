package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class LoginResultDTO extends ResponseDTO {
    private UserDto user;
    private String token;
    private LocationDto location;
    private boolean admin;

    public LoginResultDTO(boolean success, String token, String errorMessage, UserDto user, LocationDto location, boolean admin) {
        super(success, errorMessage);
        this.user = user;
        this.location = location;
        this.token = token;
        this.admin = admin;
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

    public boolean isAdmin() {
        return admin;
    }


    @Override
    public String toString() {
        return "LoginResultDTO{" +
                "user=" + user +
                ", token='" + token + '\'' +
                ", location=" + location +
                ", admin=" + admin +
                '}';
    }
}
