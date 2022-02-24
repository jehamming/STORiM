package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetRoomResultDTO implements  ResponseDTO {

    private RoomDto room;

    public GetRoomResultDTO( RoomDto room) {

        this.room = room;
    }

    public RoomDto getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "GetRoomResultDTO{" +
                "room=" + room +
                '}';
    }
}
