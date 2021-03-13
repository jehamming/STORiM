package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

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
