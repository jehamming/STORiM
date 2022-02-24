package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;

public class GetRoomsForServerResponseDTO implements  ResponseDTO {

    private final HashMap<Long, String> rooms;

    public GetRoomsForServerResponseDTO(HashMap<Long, String> rooms) {
        this.rooms = rooms;
    }

    public HashMap<Long, String> getRooms() {
        return rooms;
    }
}
