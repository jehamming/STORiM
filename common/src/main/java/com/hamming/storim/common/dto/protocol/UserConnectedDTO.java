package com.hamming.storim.common.dto.protocol;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;

public class UserConnectedDTO implements ProtocolDTO {

    private UserDto user;
    private LocationDto location;

    public UserConnectedDTO(UserDto user, LocationDto location) {
        this.user = user;
        this.location = location;
    }

    public UserDto getUser() {
        return user;
    }

    public LocationDto getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "UserConnectedDTO{" +
                "user=" + user +
                ", location=" + location +
                '}';
    }
}
