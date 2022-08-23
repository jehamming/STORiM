package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ConnectResultDTO extends ResponseDTO {

    private boolean connectSucceeded = false;
    private String errorMessage;
    private UserDto user;
    private LocationDto location;

    public ConnectResultDTO(boolean success, String errorMessage, UserDto user, LocationDto location) {
        this.connectSucceeded = success;
        this.errorMessage = errorMessage;
        this.user = user;
        this.location = location;
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

    public boolean isConnectSucceeded() {
        return connectSucceeded;
    }

    @Override
    public String toString() {
        return "ConnectResultDTO{" +
                "connectSucceeded=" + connectSucceeded +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                ", location=" + location +
                '}';
    }
}
