package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class RoomDeletedDTO implements ProtocolDTO {

    private Long roomId;

    public RoomDeletedDTO(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "RoomDeletedDTO{" +
                "roomId=" + roomId +
                '}';
    }
}
