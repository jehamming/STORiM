package com.hamming.storim.common.dto.protocol;

import com.hamming.storim.common.dto.LocationDto;

public class UserTeleportedDTO extends ProtocolResponseDTO {

    private Long userId;
    private LocationDto location;
    private Long fromRoomId;

    public UserTeleportedDTO(long userId, Long fromRoomId,  LocationDto location) {
        this.userId = userId;
        this.location = location;
        this.fromRoomId = fromRoomId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocationDto getLocation() {
        return location;
    }

    public Long getFromRoomId() {
        return fromRoomId;
    }

    @Override
    public String toString() {
        return "UserTeleportedDTO{" +
                "userId=" + userId +
                ", location=" + location +
                ", fromRoomId=" + fromRoomId +
                '}';
    }
}
