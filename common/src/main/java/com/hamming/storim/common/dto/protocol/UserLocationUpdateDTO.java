package com.hamming.storim.common.dto.protocol;

import com.hamming.storim.common.dto.LocationDto;

public class UserLocationUpdateDTO implements ProtocolDTO {

    private Long userId;
    private LocationDto location;

    public UserLocationUpdateDTO(long userId, LocationDto location) {
        this.userId = userId;
        this.location = location;
    }

    public LocationDto getLocation() {
        return location;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "UserLocationUpdateDTO{" +
                "userId=" + userId +
                ", location=" + location +
                '}';
    }
}
