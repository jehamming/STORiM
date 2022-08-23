package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class RoomAddedDTO extends ResponseDTO {

    private RoomDto room;

    public RoomAddedDTO(RoomDto room) {
        this.room = room;
    }

    public RoomDto getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "RoomAddedDTO{" +
                "room=" + room +
                '}';
    }
}
