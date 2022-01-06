package com.hamming.storim.common.dto.protocol.room;

import com.hamming.storim.common.dto.protocol.ProtocolASyncRequestDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolSyncRequestDTO;

public class GetRoomDTO extends ProtocolSyncRequestDTO {

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
