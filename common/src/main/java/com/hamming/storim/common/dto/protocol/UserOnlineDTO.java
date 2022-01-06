package com.hamming.storim.common.dto.protocol;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;

public class UserOnlineDTO extends ProtocolResponseDTO {

    private UserDto user;
    private LocationDto location;

    public UserOnlineDTO(UserDto user, LocationDto location) {
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
        return "UserOnlineDTO{" +
                "user=" + user +
                ", location=" + location +
                '}';
    }
}
