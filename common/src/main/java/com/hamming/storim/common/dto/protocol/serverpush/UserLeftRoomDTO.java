package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UserLeftRoomDTO extends ResponseDTO {


    private Long userId;
    private Long newRoomId;
    private String newRoomName;
    private String userName;


    public UserLeftRoomDTO(Long userId, String userName, Long newRoomId, String newRoomName) {
        super(true, null);
        this.userId = userId;
        this.newRoomId = newRoomId;
        this.newRoomName = newRoomName;
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getNewRoomId() {
        return newRoomId;
    }

    public String getNewRoomName() {
        return newRoomName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "UserLeftRoomDTO{" +
                "userId=" + userId +
                ", newRoomId=" + newRoomId +
                ", newRoomName='" + newRoomName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
