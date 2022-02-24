package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

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
