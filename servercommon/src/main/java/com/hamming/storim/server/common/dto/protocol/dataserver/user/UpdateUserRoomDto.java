package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateUserRoomDto implements ProtocolDTO {

    private Long userId;
    private Long roomId;

    public UpdateUserRoomDto(Long userId, Long roomId){
        this.userId = userId;
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "UpdateUserLocationDto{" +
                "userId=" + userId +
                ", roomId=" + roomId +
                '}';
    }
}
