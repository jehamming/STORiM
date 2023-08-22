package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRoomsForUserResponseDTO extends ResponseDTO {

    private final HashMap<Long, String> rooms;

    public GetRoomsForUserResponseDTO(boolean success, HashMap<Long, String> rooms, String errorMessage) {
        super(success, errorMessage);
        this.rooms = rooms;
    }

    public HashMap<Long, String> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        return "GetRoomsForUserResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
