package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UserLocationUpdatedDTO implements  ResponseDTO {

    private Long userId;
    private LocationDto location;
    private Long sequenceNumber;

    public UserLocationUpdatedDTO(Long userId, LocationDto location, Long sequenceNumber) {
        this.userId = userId;
        this.location = location;
        this.sequenceNumber = sequenceNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public LocationDto getLocation() {
        return location;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public String toString() {
        return "UserLocationUpdatedDTO{" +
                "userId=" + userId +
                ", location=" + location +
                '}';
    }
}
                                                                                                                                          