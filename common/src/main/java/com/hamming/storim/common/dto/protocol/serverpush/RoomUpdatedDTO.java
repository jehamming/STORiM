package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class RoomUpdatedDTO extends ResponseDTO {


    private RoomDto room;

    public RoomUpdatedDTO(RoomDto room) {
        super(true, null);
        this.room = room;
    }

    public RoomDto getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "RoomUpdatedDTO{" +
                "room=" + room +
                '}';
    }
}
