package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.RoomDto;

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
