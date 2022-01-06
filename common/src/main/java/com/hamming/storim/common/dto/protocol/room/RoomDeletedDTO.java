package com.hamming.storim.common.dto.protocol.room;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolResponseDTO;

public class RoomDeletedDTO extends ProtocolResponseDTO {

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
