package com.hamming.storim.common.dto.protocol.room;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteRoomDTO implements ProtocolDTO {

    private Long roomId;

    public DeleteRoomDTO(Long verbID) {
        this.roomId = verbID;
    }

    public Long getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "DeleteRoomDTO{" +
                "roomId=" + roomId +
                '}';
    }
}
