package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetRoomResultDTO extends ResponseDTO {

    private RoomDto room;

    public GetRoomResultDTO( boolean success, RoomDto room, String errorMessage) {
        super(success, errorMessage);
        this.room = room;
    }

    public RoomDto getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "GetRoomResultDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", room=" + room +
                '}';
    }
}
