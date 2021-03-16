package com.hamming.storim.model.dto.protocol.room;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class GetRoomDTO implements ProtocolDTO {

    private Long roomID;

    public GetRoomDTO(Long roomID) {
        this.roomID = roomID;
    }

    public Long getRoomID() {
        return roomID;
    }

    @Override
    public String toString() {
        return "GetRoomDTO{" +
                "roomID=" + roomID +
                '}';
    }
}
