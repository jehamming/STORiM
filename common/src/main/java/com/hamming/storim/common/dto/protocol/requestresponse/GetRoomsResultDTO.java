package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;

public class GetRoomsResultDTO extends ResponseDTO {

    private HashMap<Long, String> rooms;

    public GetRoomsResultDTO(boolean success, HashMap<Long, String> rooms, String errorMessage) {
        super(success, errorMessage);
        this.rooms = rooms;
    }

    public HashMap<Long, String> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        return "GetRoomsResultDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
