package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;
import java.util.Map;

public class GetRoomsForUserResponseDTO implements  ResponseDTO {

    private final Map<Long, String> rooms;

    public GetRoomsForUserResponseDTO(Map<Long, String> rooms) {
        this.rooms = rooms;
    }

    public Map<Long, String> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        return "GetRoomsForUserResponseDTO{" +
                "rooms=" + rooms +
                '}';
    }
}
