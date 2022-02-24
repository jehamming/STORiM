package com.hamming.storim.common.dto.protocol.serverpush.old;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class RoomDeletedDTO implements  ResponseDTO {

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
