package com.hamming.storim.common.dto.protocol.room;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetRoomResultDTO implements ProtocolDTO {

    private boolean success = false;
    private String errorMessage;
    private RoomDto room;

    public GetRoomResultDTO( boolean success, String errorMessage, RoomDto room) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.room = room;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public RoomDto getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "GetRoomResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", room=" + room +
                '}';
    }
}
