package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.RoomDto;

public class RoomUpdatedDTO implements DTO {


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
