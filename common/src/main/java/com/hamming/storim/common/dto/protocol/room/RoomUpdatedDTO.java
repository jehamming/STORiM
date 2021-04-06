package com.hamming.storim.common.dto.protocol.room;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class RoomUpdatedDTO implements ProtocolDTO {


    private RoomDto room;

    public RoomUpdatedDTO(RoomDto room) {

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
