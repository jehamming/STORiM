package com.hamming.storim.common.dto.protocol;

import com.hamming.storim.common.dto.LocationDto;

public class UserInRoomDTO extends ProtocolResponseDTO {

    private Long userId;
    private Long roomId;
    private LocationDto location;

    public UserInRoomDTO(long userId, Long roomId, LocationDto location) {
        this.userId = userId;
        this.roomId = roomId;
        this.location = location;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocationDto getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "UserInRoomDTO{" +
                "userId=" + userId +
                ", roomId=" + roomId +
                ", location=" + location +
                '}';
    }
}
