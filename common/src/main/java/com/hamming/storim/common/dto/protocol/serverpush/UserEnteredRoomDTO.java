package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UserEnteredRoomDTO extends ResponseDTO {


    private UserDto user;
    private LocationDto location;
    private Long oldRoomId;
    private String oldRoomName;

    public UserEnteredRoomDTO(UserDto user, LocationDto location, Long oldRoomId, String oldRoomName) {
        super(true, null);
        this.user = user;
        this.location = location;
        this.oldRoomId = oldRoomId;
        this.oldRoomName = oldRoomName;
    }

    public UserDto getUser() {
        return user;
    }

    public LocationDto getLocation() {
        return location;
    }

    public Long getOldRoomId() {
        return oldRoomId;
    }

    public String getOldRoomName() {
        return oldRoomName;
    }

    @Override
    public String toString() {
        return "UserEnteredRoomDTO{" +
                "user=" + user +
                ", location=" + location +
                ", oldRoomId=" + oldRoomId +
                ", oldRoomName='" + oldRoomName + '\'' +
                '}';
    }
}
