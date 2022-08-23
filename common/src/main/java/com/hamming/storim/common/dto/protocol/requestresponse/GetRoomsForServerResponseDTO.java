package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;

public class GetRoomsForServerResponseDTO extends ResponseDTO {

    private final HashMap<Long, String> rooms;

    public GetRoomsForServerResponseDTO(HashMap<Long, String> rooms) {
        this.rooms = rooms;
    }

    public HashMap<Long, String> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        return "GetRoomsForServerResponseDTO{" +
                "rooms=" + rooms +
                '}';
    }
}
