package com.hamming.storim.model.dto.protocol.room;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

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
