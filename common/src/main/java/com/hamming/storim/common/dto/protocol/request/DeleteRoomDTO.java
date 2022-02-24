package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteRoomDTO implements ProtocolDTO {

    private Long roomId;

    public DeleteRoomDTO(Long roomId) {
        this.roomId = roomId;
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
