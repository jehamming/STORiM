package com.hamming.storim.common.dto.protocol.serverpush.old;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ThingInRoomDTO implements  ResponseDTO {


    private ThingDto thing;
    private RoomDto room;

    public ThingInRoomDTO(ThingDto thing, RoomDto room) {
        this.thing = thing;
        this.room = room;
    }

    public ThingDto getThing() {
        return thing;
    }

    public RoomDto getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "ThingInRoomDTO{" +
                "thing=" + thing +
                ", room=" + room +
                '}';
    }
}