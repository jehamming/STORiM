package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;
import java.util.Map;

public class GetRoomsForUserResponseDTO extends ResponseDTO {

    private final Map<Long, String> rooms;

    public GetRoomsForUserResponseDTO(boolean success, Map<Long, String> rooms, String errorMessage) {
        super(success, errorMessage);
        this.rooms = rooms;
    }

    public Map<Long, String> getRooms() {
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
