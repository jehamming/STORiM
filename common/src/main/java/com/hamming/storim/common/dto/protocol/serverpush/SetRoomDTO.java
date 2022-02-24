package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class SetRoomDTO implements ResponseDTO {

    private RoomDto room;

    public SetRoomDTO(RoomDto room) {
        this.room = room;
    }

    public RoomDto getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "SetRoomDTO{" +
                "room=" + room +
                '}';
    }
}
