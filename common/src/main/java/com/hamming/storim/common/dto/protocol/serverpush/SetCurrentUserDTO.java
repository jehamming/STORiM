package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class SetCurrentUserDTO extends ResponseDTO {

    private UserDto user;
    private LocationDto location;

    public SetCurrentUserDTO(UserDto user, LocationDto location) {
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
        return "SetCurrentUserDTO{" +
                "user=" + user +
                ", location=" + location +
                '}';
    }
}
                                                                                                                                          