package com.hamming.storim.model.dto.protocol.room;

import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class RoomAddedDTO implements ProtocolDTO {


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
